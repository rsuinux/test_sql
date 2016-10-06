package org.suinot.test_sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by remi on 03/10/16.
 */

class ListItem {
    public String medicament;
    public String dosage;

    // default constructor !!! attention: si je ne place pas les trois lignes ci dessous, il faut initialiser les données dès le début
    // public ListItem() {
    //    this("medicament", "dosage");
    //}

    // main constructor
    public ListItem(String medicament, String dosage) {
        super ();
        this.medicament = medicament;
        this.dosage = dosage;
    }

    // String representation
    public String toString() {
        return this.medicament + " : " + this.dosage;
    }
    // Map interface classes

    // return a count of our members
    public int size() {
        return 2;
    }

    // set the values of the object to null
    public void clear() {
        this.medicament = null;
        this.dosage = null;
    }

    // return all of the values as a collection
    public ArrayList<String> values() {
        ArrayList<String> list = new ArrayList<String> ();

        list.add (medicament);
        list.add (dosage);

        return list;
    }

    // if the values of the members are null, return true
    public boolean isEmpty() {
        if ((this.medicament == null) && (this.dosage == null)) {
            return true;
        } else {
            return false;
        }
    }

    // return a set of the members
    public Set<String> keySet() {
        Set<String> s = new HashSet<String> ();

        s.add ("medicament");
        s.add ("dosage");

        return s;
    }

    // return a set of the member values
    public Set entrySet() {
        Set<String> s = new HashSet<String> ();

        s.add (this.medicament);
        s.add (this.dosage);

        return s;
    }

    // return the value of the given key
    public String get(Object key) {
        if (key.equals ("medicament")) {
            return this.medicament;
        }
        if (key.equals ("dosage")) {
            return this.dosage;
        }
        // if we can't return a value, throw the exception
        throw new ClassCastException ();
    }

    // set the value of a given key
    public String put(String key, String value) {
        if (key.equals ("medicament")) {
            this.medicament = value;
        }
        if (key.equals ("dosage")) {
            this.dosage = value;
        }
        return value;
    }

    // remove a key (nullify)
    public String remove(Object key) {
        String value = null;
        if (key.equals ("medicament")) {
            value = this.medicament;
            this.medicament = null;
        }
        if (key.equals ("dosage")) {
            value = this.dosage;
            this.dosage = null;
        }
        return value;
    }

    // return boolean if we have a member
    public boolean containsKey(Object key) {
        if (key.equals ("medicament")) {
            return true;
        }
        if (key.equals ("dosage")) {
            return true;
        }
        return false;
    }

    // return boolean if we have a member's value
    public boolean containsValue(Object value) {
        if (value.equals (this.medicament)) {
            return true;
        }
        if (value.equals (this.dosage)) {
            return true;
        }
        return false;
    }

    // set the values of this map to that of another
    public void putAll(Map<? extends String, ? extends String> arg0) {
        // we only need the stub.
    }
}
