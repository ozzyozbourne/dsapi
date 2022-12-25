package io.github.ozzyozbourne;

public enum GDEnums {

    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    PDF("application/pdf"),
    TXT("text/plain"),
    CSV("text/csv"),
    JPEG("image/jpeg"),
    PNG("image/png"),
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
