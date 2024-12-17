package wi.roger.rogerWI.types;

import lombok.Getter;

public class CommonEnums {

    public enum EducatorRole {
        TEACHER,
        COUNSELOR,
        ADMINISTRATOR,
        PARENT,
        OTHER
    }

    public enum AVRequirement {
        WIFI,
        PROJECTOR,
        AUDIO,
        HDMI,
        APPLE_ADAPTER,
        OTHER
    }

    public enum TourType {
        BASIC,
        ADVANCED
    }

    public enum UserType {
        ADMIN,
        REGIONAL_LEADS,
        COORDINATOR,
        EDUCATOR,
        STUDENT,
        COMPANY_ADMIN
    }

    public enum PhoneType {
        CELL,
        HOME,
        SCHOOL
    }

    public enum GradeLevel {
        PRE_K,
        ELEMENTARY,
        MIDDLE_SCHOOL,
        HIGH_SCHOOL,
        COLLEGE_POST_SECONDARY
    }

    public enum Grade {
        PreK,
        Fifth,
        Sixth,
        Seventh,
        Eighth,
        Ninth,
        Tenth,
        Eleventh,
        Twelfth,
        Post_Secondary
    }

    public enum ActivityType {
        REQUEST,
        RECORD
    }

    public enum DeliveryMode {
        IN_PERSON,
        VIRTUAL
    }

    public enum ActivityCategory {
        VIRTUAL_COMPANY_TOUR,
        VIRTUAL_GUEST_SPEAKER,
        VIRTUAL_JOB_SHADOW,
        VIRTUAL_MOCK_INTERVIEW,
        CAREER_FAIR,
        COMPANY_TOUR,
        EDUCATOR_EXTERNSHIP,
        GUEST_SPEAKER,
        JOB_SHADOW,
        MENTOR,
        MOCK_INTERVIEW,
        PART_TIME_JOB,
        PROJECT_BASED_SUPPORT,
        VOLUNTEER,
        YOUTH_APPRENTICESHIP,
        YOUTH_COOP
    }

    @Getter
    public enum AccessLevel {
        EDUCATOR_ACCESS("Educator Access"),
        STUDENT_ACCESS("Student Access"),
        IN_PERSON_ACTIVITIES("In Person Activities"),
        VIRTUAL_ACTIVITIES("Virtual Activities"),
        IN_PERSON_EVENTS("In Person Events"),
        VIRTUAL_EVENTS("Virtual Events"),
        PATHWAYS("Pathways");

        private final String displayName;

        AccessLevel(String displayName) {
            this.displayName = displayName;
        }
    }

    @Getter
    public enum AccessType {
        FULL_SERVICE("Full Service"),
        VIRTUAL("Virtual"),
        M7_EDUCATOR("M7-Educator");

        private final String displayName;

        AccessType(String displayName) {
            this.displayName = displayName;
        }
    }

    @Getter
    public enum County {
        BROWN("Brown"),
        CALUMET("Calumet"),
        COLUMBIA("Columbia"),
        DODGE("Dodge"),
        FOND_DU_LAC("Fond du Lac"),
        GREEN_LAKE("Green Lake"),
        KENOSHA("Kenosha"),
        LANGLADE("Langlade"),
        LINCOLN("Lincoln"),
        MANITOWOC("Manitowoc"),
        MARATHON("Marathon"),
        MARINETTE("Marinette"),
        MILWAUKEE("Milwaukee"),
        MPS("MPS"),
        ONEIDA("Oneida"),
        OUTAGAMIE("Outagamie"),
        OZAUKEE_M7("Ozaukee M7"),
        OZAUKEE("Ozaukee"),
        PORTAGE("Portage"),
        RACINE("Racine"),
        SHEBOYGAN("Sheboygan"),
        WAUPACA("Waupaca"),
        WAUSHARA("Waushara"),
        WALWORTH("Walworth"),
        WASHINGTON_M7("Washington M7"),
        WAUKESHA("Waukesha"),
        WINNEBAGO("Winnebago");

        private final String displayName;

        County(String displayName) {
            this.displayName = displayName;
        }
    }

    @Getter
    public enum Cluster {
        AGRICULTURE_FOOD_NATURAL_RESOURCES("Agriculture, Food and Natural Resources"),
        ARCHITECTURE_CONSTRUCTION("Architecture and Construction"),
        ARTS_AV_TECHNOLOGY_COMMUNICATIONS("Arts, A/V Technology & Communications"),
        BUSINESS_ADMIN_FINANCE_MARKETING("Business Administration, Finance & Marketing"),
        EDUCATION_TRAINING("Education & Training"),
        ENERGY("Energy"),
        GOVERNMENT_PUBLIC_ADMIN("Government & Public Administration"),
        HEALTH_SCIENCE("Health Science"),
        HOSPITALITY_CULINARY_TOURISM("Hospitality, Culinary and Tourism"),
        HUMAN_SERVICES("Human Services"),
        INFORMATION_TECHNOLOGY("Information Technology"),
        LAW_PUBLIC_SAFETY_CORRECTIONS_SECURITY("Law, Public Safety, Corrections & Security"),
        MANUFACTURING("Manufacturing"),
        STEM("Science, Technology, Engineering & Mathematics"),
        TRANSPORTATION_DISTRIBUTION_LOGISTICS("Transportation, Distribution & Logistics");

        private final String displayName;

        Cluster(String displayName) {
            this.displayName = displayName;
        }

    }

    @Getter
    public enum AccessTo {
        FULL_SERVICE("Full Service"),    // all cluster/pathway and activity
        VIRTUAL_ONLY("Virtual Only"),    // Virtual activities only
        M7("M7");                       // only Educator activities

        private final String displayName;

        AccessTo(String displayName) {
            this.displayName = displayName;
        }
    }

    @Getter
    public enum QuestionType {
        SINGLE_CHOICE("single"),
        MULTIPLE_CHOICE("multiple"),
        TEXT("text"),
        DATE("date"),
        TIME("time"),
        EMAIL("email"),
        PHONE("phone"),
        SCHOOL_SELECT("school"),
        COMPANY_SELECT("company"),
        ACTIVITY_SELECT("activity");

        private final String value;

        QuestionType(String value) {
            this.value = value;
        }
    }

}
