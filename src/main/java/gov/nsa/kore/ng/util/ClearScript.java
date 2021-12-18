package gov.nsa.kore.ng.util;

import meteordevelopment.starscript.StandardLib;
import meteordevelopment.starscript.Starscript;
import meteordevelopment.starscript.value.Value;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

public class ClearScript extends Starscript {
    private static final Field globals;
    static {
        try {
            globals = Starscript.class.getDeclaredField("globals");
            globals.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public ClearScript() {
        StandardLib.init(this);
    }

    public void clear() {
        getGlobals().clear();
        StandardLib.init(this);
    }

    private Map<String, Supplier<Value>> getGlobals() {
        try {
            return (Map<String, Supplier<Value>>) globals.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
