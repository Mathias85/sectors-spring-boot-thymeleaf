package app.mathias.sector.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public final class MessageHelper implements ApplicationContextAware {

    private static MessageHelper INSTANCE;
    private final MessageSource messageSource;

    private MessageHelper(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // This method will be called by Spring to inject the context
    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) {
        if (INSTANCE == null) {
            INSTANCE = new MessageHelper(applicationContext.getBean(MessageSource.class));
        }
    }

    // Static method to get the singleton instance
    private static MessageHelper getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("MessageHelper is not initialized");
        }
        return INSTANCE;
    }

    // Method to fetch message from messages.properties
    public static String getMessage(final String code, final Object... args) {
        return getInstance().messageSource.getMessage(code, args, Locale.getDefault());
    }
}
