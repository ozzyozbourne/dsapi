package io.github.ozzyozbourne.enums;


/**
 * ENUMS for google drive
 */
public enum GDEnums {

    /**
     *  MIME TYPE FOR DOCX
     */
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),

    /**
     *  MIME TYPE FOR XLSX
     */
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),

    /**
     *  MIME TYPE FOR PDF
     */
    PDF("application/pdf"),

    /**
     *  MIME TYPE FOR TXT
     */
    TXT("text/plain"),

    /**
     *  MIME TYPE FOR CSV
     */
    CSV("text/csv"),

    /**
     *  MIME TYPE FOR JPEG
     */
    JPEG("image/jpeg"),

    /**
     *  MIME TYPE FOR PNG
     */
    PNG("image/png"),

    /**
     *  MIME TYPE FOR SVG
     */
    SVG("image/svg+xml");

    private final String name;

    GDEnums(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
