package split;

import org.junit.jupiter.api.Test;

class SplitTest {

    @Test
    void main() {
        Split.main("-d -l 1 -o - src/test/files/123.txt".split(" "));

    }
}