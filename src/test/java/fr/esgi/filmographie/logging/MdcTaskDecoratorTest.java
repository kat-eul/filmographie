package fr.esgi.filmographie.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class MdcTaskDecoratorTest {

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldPropagateMdcToDecoratedRunnable_andRestorePreviousAfterRun() {
        MDC.put("correlationId", "cid-1");

        final var decorator = new MdcTaskDecorator();

        final AtomicReference<String> seenInside = new AtomicReference<>();
        final Runnable task = () -> seenInside.set(MDC.get("correlationId"));

        final Runnable decorated = decorator.decorate(task);

        MDC.put("correlationId", "executor-prev");

        decorated.run();

        assertThat(seenInside.get()).isEqualTo("cid-1");
        assertThat(MDC.get("correlationId")).isEqualTo("executor-prev");
    }

    @Test
    void shouldClearMdcInsideRunnableWhenCapturedContextIsNull_andRestorePreviousAfterRun() {
        MDC.clear();

        final var decorator = new MdcTaskDecorator();

        final AtomicReference<Map<String, String>> seenInside = new AtomicReference<>();
        final Runnable task = () -> seenInside.set(MDC.getCopyOfContextMap());

        final Runnable decorated = decorator.decorate(task);

        final Map<String, String> previous = new HashMap<>();
        previous.put("k", "v");
        MDC.setContextMap(previous);

        decorated.run();

        assertThat(seenInside.get()).isNull();
        assertThat(MDC.getCopyOfContextMap()).containsEntry("k", "v");
    }

    @Test
    void shouldRestoreToEmptyWhenPreviousWasNull() {
        MDC.put("correlationId", "cid-1");

        final var decorator = new MdcTaskDecorator();

        final AtomicReference<String> seenInside = new AtomicReference<>();
        final Runnable task = () -> seenInside.set(MDC.get("correlationId"));

        final Runnable decorated = decorator.decorate(task);

        MDC.clear();

        decorated.run();

        assertThat(seenInside.get()).isEqualTo("cid-1");
        assertThat(MDC.getCopyOfContextMap()).isNull();
    }
}

