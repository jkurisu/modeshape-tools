/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.rest.preferences;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class IgnoredResourcesModelTest {

    private final static int NUM_PATTERNS = 10;

    private final static ResourcePattern[] ENABLED_PATTERNS = new ResourcePattern[NUM_PATTERNS];

    private final static ResourcePattern[] DISABLED_PATTERNS = new ResourcePattern[NUM_PATTERNS];

    static {
        for (int i = 0; i < NUM_PATTERNS; ++i) {
            ENABLED_PATTERNS[i] = new ResourcePattern(i + "*.class", true); //$NON-NLS-1$
            DISABLED_PATTERNS[i] = new ResourcePattern(i + "*.class", false); //$NON-NLS-1$
        }
    }

    private IgnoredResourcesModel model;

    @Before
    public void beforeEach() {
        this.model = new IgnoredResourcesModel();
    }

    @Test
    public void shouldClearPatternsWhenLoading() {
        this.model.load(IgnoredResourcesModel.createList(DISABLED_PATTERNS));
        this.model.load(IgnoredResourcesModel.createList(ENABLED_PATTERNS));
        assertEquals(NUM_PATTERNS, this.model.getPatterns().size());
    }

    @Test
    public void shouldHaveNoPatternsAfterConstruction() {
        assertTrue(this.model.getPatterns().isEmpty());
    }

    @Test
    public void shouldLoadEmptyString() {
        this.model.load(""); //$NON-NLS-1$
        assertTrue(this.model.getPatterns().isEmpty());
    }

    @Test
    public void shouldLoadNullString() {
        this.model.load(null);
        assertTrue(this.model.getPatterns().isEmpty());
    }

    @Test
    public void shouldNotLoadSamePatternTwice() {
        this.model.load(IgnoredResourcesModel.createList(DISABLED_PATTERNS[0]));
        this.model.load(IgnoredResourcesModel.createList(DISABLED_PATTERNS[0]));
        assertEquals(1, this.model.getPatterns().size());

        this.model.load(IgnoredResourcesModel.createList(ENABLED_PATTERNS[0]));
        assertEquals(1, this.model.getPatterns().size());
    }

    @Test
    public void shouldAddNewPattern() {
        final ResourcePattern rp = DISABLED_PATTERNS[0];
        this.model.addPattern(rp);
        assertThat(this.model.getPatterns().size(), is(1));
        assertThat(rp, is(sameInstance(this.model.getPatterns().iterator().next())));
    }

    @Test
    public void shouldRemovePattern() {
        final ResourcePattern rp = DISABLED_PATTERNS[0];
        this.model.addPattern(rp);
        this.model.removePattern(rp);
        assertTrue(this.model.getPatterns().isEmpty());
    }

    @Test
    public void shouldAllowRemoveOfPatternNotInModel() {
        this.model.removePattern(DISABLED_PATTERNS[0]);
    }

}
