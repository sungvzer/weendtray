package it.garganovolpe.weendtray.logging;

import java.lang.reflect.Field;

public class LoggerInjector {
    public static void inject(Object target) {
        Class<?> clazz = target.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(GetLoggerProviderFromEnv.class)) {
                String envType = System.getenv("APP_LOGGER");
                if (envType == null) {
                    envType = field.getAnnotation(GetLoggerProviderFromEnv.class).defaultType();
                }

                LoggerProvider provider = switch (envType.toUpperCase()) {
                    case "DATABASE" -> new DatabaseLoggerProvider();
                    case "COMBINED" -> new CombinedLoggerProvider();
                    default -> new ConsoleLoggerProvider();
                };

                try {
                    field.setAccessible(true);
                    field.set(target, provider);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Errore nell'iniezione del Logger", e);
                }
            }
        }
    }
}
