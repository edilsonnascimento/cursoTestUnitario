package br.ce.enascimento.matchers;

import java.util.Calendar;

public class CoreMatcherProprio {

    public static DiaSemanaMatcher caiEm(Integer diaSemana){
        return new DiaSemanaMatcher(diaSemana);
    }
    public static DiaSemanaMatcher caiNaSegunda(){
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }
}
