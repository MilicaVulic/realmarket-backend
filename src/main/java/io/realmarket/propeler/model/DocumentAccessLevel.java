package io.realmarket.propeler.model;

import io.realmarket.propeler.model.enums.EDocumentAccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "document_access_level",
    indexes = {
      @Index(columnList = "name", unique = true, name = "document_access_level_uk_on_name"),
    })
public class DocumentAccessLevel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_ACCESS_LEVEL_SEQ")
  @SequenceGenerator(
      name = "DOCUMENT_ACCESS_LEVEL_SEQ",
      sequenceName = "DOCUMENT_ACCESS_LEVEL_SEQ",
      allocationSize = 1)
  private Long id;

  @Enumerated(EnumType.STRING)
  private EDocumentAccessLevel name;
}
