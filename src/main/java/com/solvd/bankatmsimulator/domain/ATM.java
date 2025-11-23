package com.solvd.bankatmsimulator.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ATM {

    private Long id;
    private String location;
    private String name;
    private boolean isActive;
    private List<ATMBanknote> banknotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ATMBanknote> getBanknotes() {
        if (banknotes == null) {
            banknotes = new ArrayList<>();
        }
        return banknotes;
    }

    public void setBanknotes(List<ATMBanknote> banknotes) {
        this.banknotes = banknotes;
    }

    public void addBanknote(ATMBanknote banknote) {
        getBanknotes().add(banknote);
    }

    public void removeBanknote(ATMBanknote banknote) {
        getBanknotes().remove(banknote);
    }

    @Override
    public String toString() {
        return "ATM{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", banknotesCount=" + (banknotes != null ? banknotes.size() : 0) +
                '}';
    }
}

