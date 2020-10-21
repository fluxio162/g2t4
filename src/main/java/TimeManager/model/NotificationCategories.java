package TimeManager.model;

/**
 * Enumeration of available notification categories/ intervals.
 */
public enum NotificationCategories {
    NEVER{
        @Override
        public String toString() {
            return "Nie";
        }
    },
    WEEKLY{
        @Override
        public String toString() {
            return "Wöchentlich";
        }
    },
    MONTHLY{
        @Override
        public String toString() {
            return "Monatlich";
        }
    },
    YEARLY{
        @Override
        public String toString() {
            return "Jährlich";
        }
    },

}
