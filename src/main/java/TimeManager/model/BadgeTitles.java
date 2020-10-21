package TimeManager.model;

/**
 * Enumeration of available badge titles and their corresponding task categories.
 */
public enum BadgeTitles {
    DESIGN{
        @Override
        public String toString() {
            return "Company's Own Bob Ross";
        }
    },
    IMPLEMENTATION{
        @Override
        public String toString() {
            return "Insert Coffee to Continue";
        }
    },
    TESTING{
        @Override
        public String toString() {
            return "Testing is Love, Testing is Life";
        }
    },
    ERROR_ANALYSIS{
        @Override
        public String toString() {
            return "Exterminator";
        }
    },
    CUSTOMER_MEETING{
        @Override
        public String toString() {
            return "Communication is Key";
        }
    },
}
