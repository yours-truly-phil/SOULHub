package io.horrorshow.soulhub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface HasLogger {

    default Logger LOGGER() {
        return LoggerFactory.getLogger(getClass());
    }
}
