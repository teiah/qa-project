package com.telerikacademy.testframework.utils;

import lombok.Getter;

@Getter
public enum Expertise {
    ALL("All", 100),
    ACCOUNTANT("Accountant", 101),
    ACTOR("Actor", 102),
    ARCHAEOLOGIST("Archaeologist", 103),
    ASTRONAUT("Astronaut", 104),
    AUTHOR("Author", 105),
    BAKER("Baker", 106),
    BUS_DRIVER("Bus driver", 107),
    CAR_SERVICE("Car service", 108),
    CHEF("Chef/Cook", 109),
    CLEANER("Cleaner", 110),
    DENTIST("Dentist", 111),
    DESIGNER("Designer", 112),
    DOCTOR("Doctor", 113),
    ELECTRICIAN("Electrician", 114),
    FACTORY_WORKER("Factory worker", 115),
    FARMER("Farmer", 116),
    FIREFIGHTER("Firefighter", 117),
    FISHERMAN("Fisherman", 118),
    GARDENER("Gardener", 119),
    HAIRDRESSER("Hairdresser", 120),
    JOURNALIST("Journalist", 121),
    JUDGE("Judge", 122),
    LAWYER("Lawyer", 123),
    LECTURER("Lecturer", 124),
    LIFEGUARD("Lifeguard", 125),
    MECHANIC("Mechanic", 126),
    MODEL("Model", 127),
    MUSICIAN("Musician", 128),
    NANNY("Nanny", 129),
    NEWSREADER("Newsreader", 130),
    NURSES("Nurses", 131),
    OPTICIAN("Optician", 132),
    PAINTER("Painter", 133),
    PHARMACIST("Pharmacist", 134),
    PHOTOGRAPHER("Photographer", 135),
    PILOT("Pilot", 136),
    PLUMBER("Plumber", 137),
    POLICE_OFFICER("Police Officer", 138),
    POLICEMAN("Policeman", 139),
    POLITICIAN("Politician", 140),
    POSTMAN("Postman", 141),
    REAL_ESTATE_AGENT("Real estate agent", 142),
    RECEPTIONIST("Receptionist", 143),
    SCIENTIST("Scientist", 144),
    SECRETARY("Secretary", 145),
    SHOP_ASSISTANT("Shop assistant", 146),
    SOFTWARE_DEVELOPER("Software developer", 147),
    SOLDIER("Soldier", 148),
    TAILOR("Tailor", 149),
    TEACHERS("Teachers", 150),
    TRANSLATOR("Translator", 151),
    TRAVEL_AGENT("Travel agent", 152),
    VETERINARIAN("Veterinarian", 153),
    WAITER_WAITRESS("Waiter/Waitress", 154),
    WINDOW_CLEANER("Window cleaner", 155),
    WRITER("Writer", 156),
    MARKETING("Marketing", 157);

    public final String stringValue;
    public final int id;
    Expertise(String stringValue, int id) {
        this.stringValue = stringValue;
        this.id = id;
    }
}
