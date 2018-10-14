package com.alexa4.linguistic_project.data_stores;

/**
 * LIst of means of expressiveness
 * text field is a parameter which need to display means in text form
 */
public enum MeansOfExpressiveness{
    METAPHOR ("metaphor"),
    SIMILE ("simile"),
    EPITHET ("epithet"),
    EUPHEMISM ("euphemism"),
    PARADOX ("paradox"),
    PUN ("pun"),
    ANTITHESIS ("antithesis"),
    ALLUSION ("allusion"),
    METONYMY ("metonymy"),
    REPETITION ("repetition"),
    PARALLELISM ("parallelism"),
    DETACHMENT ("detachment"),
    ONOMATOPOEIA ("onomatopoeia"),
    ELLIPSIS ("ellipsis"),
    POLYSYNDETON ("polysyndeton"),
    PERSONIFICATION ("personification"),
    GRAPHON ("graphon"),
    HYPERBOLE ("hyperbole"),
    RHETORICAL_QUESTION ("rhetorical_question"),
    INVERSION ("inversion");

    private final String text;
    MeansOfExpressiveness(String text){
        this.text = text;
    }
    public String getText() {
        return text;
    }
}
