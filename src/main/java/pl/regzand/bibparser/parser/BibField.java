package pl.regzand.bibparser.parser;

import pl.regzand.bibparser.entries.BibEntry;

import java.lang.annotation.*;

/**
 * Annotation that marks fields in {@link BibEntry BibEntry} classes that are injected by parser with value based on {@link BibField#name() name()}
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BibField {

    /**
     * @return name of a field in entry, which value will be injected in to annotated field
     */
    String name();

    /**
     * @return if this field is required
     */
    boolean required() default false;

    /**
     * @return ir this field should be parsed as set of names
     */
    boolean names() default false;

}
