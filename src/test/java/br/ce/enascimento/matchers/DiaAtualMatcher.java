package br.ce.enascimento.matchers;

import br.ce.enascimento.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

public class DiaAtualMatcher extends TypeSafeMatcher<Date> {

    private Integer dias;

    public DiaAtualMatcher(Integer dias) {
        this.dias = dias;
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(new Date(), DataUtils.obterDataComDiferencaDias(dias));
    }

    @Override
    public void describeTo(Description description) {

    }
}
