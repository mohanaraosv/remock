package no.saua.remock.exampletests.application;

import org.springframework.stereotype.Component;

@Component
public class AnInterfaceImplTwo implements AnInterface {
    @Override
    public String someMethod() {
        return "someMethodTwo";
    }
}
