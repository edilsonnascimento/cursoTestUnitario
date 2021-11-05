package br.ce.enascimento.matchers;

import static br.ce.enascimento.utils.DataUtils.*;
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
        return DataUtils.isMesmaData(data, obterDataComDiferencaDias(dias));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(dataFormatada(obterDataComDiferencaDias(dias)));
    }
}
