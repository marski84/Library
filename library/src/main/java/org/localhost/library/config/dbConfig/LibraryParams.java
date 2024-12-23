package org.localhost.library.config.dbConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "library_params")
@Getter
@Setter
@NoArgsConstructor
public class LibraryParams {
    @Id
    private String key;
    private Integer value;
    private String description;
    private ZonedDateTime lastUpdated;
}