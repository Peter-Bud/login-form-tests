package com.example.logintests.business;

import com.example.logintests.support.BooleanValidator;
import com.example.logintests.support.TextValidator;
import org.assertj.core.api.SoftAssertions;

public abstract class BaseBO {

    protected final SoftAssertions softly = new SoftAssertions();
    protected final BooleanValidator booleanValidator = new BooleanValidator();
    protected final TextValidator textValidator = new TextValidator();

}
