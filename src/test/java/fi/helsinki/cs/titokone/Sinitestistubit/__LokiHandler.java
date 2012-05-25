package fi.helsinki.cs.titokone.Sinitestistubit;

import java.util.logging.*;

public class __LokiHandler extends Handler {
    private __LoguserTestCase listener;
    private boolean hushed = false;

    public __LokiHandler(__LoguserTestCase listener) {
	this.listener = listener;
    }

    public void flush() {}

    public void close() {}

    public void publish(LogRecord record) {
	listener.logged(record);
    }

    public void hush() {
	hushed = true;
    }
}
