package io.realmarket.propeler.model.enums;

public enum FileType {
  WILD_CARDS("WILD_CARDS"),
  PROFORMA_INVOICE("PROFORMA_INVOICE"),
  INVOICE("INVOICE"),
  OFFPLATFORM_PROFORMA_INVOICE("OFFPLATFORM_PROFORMA_INVOICE"),
  OFFPLATFORM_INVOICE("OFFPLATFORM_INVOICE"),
  CONTRACT("CONTRACT");

  private final String text;

  FileType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
