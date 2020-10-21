package TimeManager.model;

/**
 * Enumeration of available user roles.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
public enum TaskCategory {

    CONCEPTION{
        @Override
        public String toString() {
            return "Konzeption";
        }
    },
    DESIGN{
        @Override
        public String toString() {
            return "Design";
        }
    },
    IMPLEMENTATION{
        @Override
        public String toString() {
            return "Implementierung";
        }
    },
    TESTING{
        @Override
        public String toString() {
            return "Testen";
        }
    },
    DOCUMENTATION{
        @Override
        public String toString() {
            return "Dokumentation";
        }
    },
    ERROR_ANALYSIS{
        @Override
        public String toString() {
            return "Fehleranalyse und -korrektur";
        }
    },
    MEETING{
        @Override
        public String toString() {
            return "Meeting (intern)";
        }
    },
    CUSTOMER_MEETING{
        @Override
        public String toString() {
            return "Kundenbesprechung";
        }
    },
    TRAINING{
        @Override
        public String toString() {
            return "Fortbildung";
        }
    },
    PROJECT_MANAGEMENT{
        @Override
        public String toString() {
            return "Projektmanagement";
        }
    },
    OTHERS{
        @Override
        public String toString() {
            return "Sonstiges";
        }
    },
    BREAK{
        @Override
        public String toString() {
            return "Pause/Dienstfrei";
        }
    }


}
