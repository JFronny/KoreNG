package gov.nsa.kore.ng.util;

import meteordevelopment.starscript.StandardLib;
import meteordevelopment.starscript.Starscript;
import meteordevelopment.starscript.value.Value;
import meteordevelopment.starscript.value.ValueMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

public class ClearScript extends Starscript {
    private static final Field globals;
    public final ValueMap global = new ValueMap();
    static {
        try {
            globals = Starscript.class.getDeclaredField("globals");
            globals.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public ClearScript() {
        clear();
    }

    public void clear() {
        getGlobals().clear();
        StandardLib.init(this);
        set("global", Value.map(global));
    }

    private Map<String, Supplier<Value>> getGlobals() {
        try {
            return (Map<String, Supplier<Value>>) globals.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Value buildValue(String content) {
        if (RegexUtil.DOUBLE.test(content))
            return Value.number(Double.parseDouble(content));
        if (RegexUtil.BOOL.test(content))
            return Value.bool(RegexUtil.TRUE.test(content));
        return Value.string(content);
    }
}
