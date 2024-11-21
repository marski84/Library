package org.localhost.library.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.localhost.library.book.dto.BookDto;
import org.localhost.library.book.dto.BookRegistrationDto;
import org.localhost.library.book.dto.EditBookDto;
import org.localhost.library.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BOOKS_URL = "/api/books";
    private static final String BOOKS_GET_URL = BOOKS_URL + "/get-books";
    private static final String BOOKS_REGISTER_URL = BOOKS_URL + "/register";
    private static final int NOT_EXISTING_BOOK_ID = 999;
    private static final int BOOK_TO_REMOVE_ID = 3;
    private static final int TEST_BOOK_ID = 1;



    @Test
    void shouldGetAllBooks() throws Exception {
        int expectedLength = bookService.getAllBooks().size();
        BookDto expectedResult = bookService.getAllBooks().get(0);
        //        when & then
        mockMvc.perform(get(BOOKS_GET_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedLength))
                .andExpect(jsonPath("$[0].title").value(expectedResult.getTitle()))
                .andExpect(jsonPath("$[0].author").value(expectedResult.getAuthor()))
                .andExpect(jsonPath("$[0].publisher").value(expectedResult.getPublisher()))
                .andExpect(jsonPath("$[0].isbn").value(expectedResult.getIsbn()))
                .andExpect(jsonPath("$[0].pages").value(expectedResult.getPages()));
    }

    @Test
    void shouldGetBookById() throws Exception {
//        when & then
        mockMvc.perform(get(BOOKS_URL +"/{id}", TEST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.publisher").value("Prentice Hall"))
                .andExpect(jsonPath("$.isbn").value("978-0132350884"))
                .andExpect(jsonPath("$.pages").value(464));
    }

    @Test
    void shouldReturn400ForNonExistentBook() throws Exception {
        mockMvc.perform(get("/api/books/{id}", NOT_EXISTING_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Book with given ID does not exist"))
                .andExpect(jsonPath("$.errorCode").value(300))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnBookAfterSuccessfulRegistration() throws Exception {
        // given
        BookRegistrationDto newBook = BookRegistrationDto.builder()
                .title("test")
                .author("test")
                .publisher("test")
                .isbn("978-0747532699")
                .pages(464)
                .build();


        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKS_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(newBook.getTitle()))
                .andExpect(jsonPath("$.author").value(newBook.getAuthor()))
                .andExpect(jsonPath("$.publisher").value(newBook.getPublisher()))
                .andExpect(jsonPath("$.isbn").value(newBook.getIsbn()))
                .andExpect(jsonPath("$.pages").value(newBook.getPages()));
    }

    @Test
    void shouldReturn400ForInvalidBookRegistration() throws Exception {
        String ERROR_MESSAGE = "Validation failed";
        String ERROR_DETAILS = "pages: Pages must be greater than 0";
        // given
        BookRegistrationDto newBook = BookRegistrationDto.builder()
                .title("test")
                .author("test")
                .publisher("test")
                .isbn("978-0747532699")
                .pages(-464)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKS_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ERROR_MESSAGE))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value(ERROR_DETAILS))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnBookDtoForValidBookEdit() throws Exception {
        // given    void shouldReturnBookDtoForValidBookEdit() throws Exception {
        EditBookDto newBook = EditBookDto.builder()
                .title("test")
                .author("test")
                .publisher("test")
                .pages(464)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch(BOOKS_URL + "/{id}", TEST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(newBook.getTitle()))
                .andExpect(jsonPath("$.author").value(newBook.getAuthor()))
                .andExpect(jsonPath("$.publisher").value(newBook.getPublisher()));
    }

    @Test
    void shouldReturn400ForInvalidBookEdit() throws Exception {
        final String ERROR_MESSAGE = "Edit dto validation failure";
        final int ERROR_CODE = 600;

        EditBookDto invalidEditDto = EditBookDto.builder()
                .title("")
                .author("")
                .publisher("")
                .pages(0)
                .build();
//        when & them
        mockMvc.perform(MockMvcRequestBuilders.patch(BOOKS_URL + "/{id}", TEST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEditDto)
                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(ERROR_MESSAGE))
                .andExpect(jsonPath("$.errorCode")
                        .value(ERROR_CODE))
                .andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    void removeBookShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BOOKS_URL + "/{id}", BOOK_TO_REMOVE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void removeBookShouldReturn400() throws Exception {
        String ERROR_MESSAGE = "Book with given ID does not exist";
        int ERROR_CODE = 300;
        mockMvc.perform(MockMvcRequestBuilders.delete(BOOKS_URL + "/{id}", NOT_EXISTING_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ERROR_MESSAGE))
                .andExpect(jsonPath("$.errorCode").value(ERROR_CODE));
    }




}