package org.sample;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Data race")
@Outcome(id = "1", expect = Expect.ACCEPTABLE, desc = "Default outcome")
public class ConcurrencyTest {

    @JCStressTest
    @JCStressMeta(ConcurrencyTest.class)
    @State
    public static class LocalVariable {

        volatile int i;

        @Actor
        public void actor1(I_Result r) {
            int a = i;
            if (a == 0) {
                synchronized (this) {
                    a = i;
                    if (a == 0) {
                        a = 1;
                        i = a;
                    }
                }
            }
            r.r1 = a;
        }

        @Actor
        public void actor2() {
            if (i != 0) {
                synchronized (this) {
                    if (i != 0) {
                        i = 0;
                    }
                }
            }
        }
    }

    @JCStressTest
    @JCStressMeta(ConcurrencyTest.class)
    @State
    public static class Plain {

        volatile int i;

        @Actor
        public void actor1(I_Result r) {
            if (i == 0) {
                synchronized (this) {
                    if (i == 0) {
                        i = 1;
                    }
                }
            }
            r.r1 = i;
        }

        @Actor
        public void actor2() {
            if (i != 0) {
                synchronized (this) {
                    if (i != 0) {
                        i = 0;
                    }
                }
            }
        }
    }
}
