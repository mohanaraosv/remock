package no.saua.remock.internal;

import java.util.Objects;

public class RejectBeanClassDefinition extends EntityHelper<RejectBeanClassDefinition> implements Rejecter {
    private final Class<?> rejectClass;

    public RejectBeanClassDefinition(Class<?> rejectClass) {
        this.rejectClass = rejectClass;
    }

    @Override
    public boolean shouldReject(String beanName, Class<?> beanClass) {
        return rejectClass.isAssignableFrom(beanClass);
    }

    @Override
    public boolean equals(RejectBeanClassDefinition other) {
        return Objects.equals(rejectClass, other.rejectClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rejectClass);
    }
}
