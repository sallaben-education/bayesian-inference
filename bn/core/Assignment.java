/*
 * File: Assignment.java
 * Creator: George Ferguson
 * Created: Tue Mar 27 13:03:29 2012
 * Time-stamp: <Wed Apr  3 18:38:46 EDT 2013 ferguson>
 */

package bn.core;

import java.util.*;

/**
 * An Assignment is a list of RandomVariables and their corresponding
 * values (Objects).
 * Currently implementation uses a LinkedHashMap to get a predictable
 * iteration ordering, which is helpful for debugging.
 */
public class Assignment extends LinkedHashMap<RandomVariable,Object> {

    public static final long serialVersionUID = 1L;
    public double weight = 1;
    
    public Assignment() {
    super();
    }

    /**
     * Set the value of the given RandomVariable stored in this Assignment.
     * This method is an alias for {@link LinkedHashMap#put}.
     */
    public void set(RandomVariable var, Object val) {
	put(var, val);
    }

    /*
     * Changes the weight of this assignment. Assignment weighting is used
     * in weighted sampling.
     */
    public void setWeight(double weight) {
    	this.weight = weight;
    }
    
    /**
     * Returns a Set view of the RandomVariables contained in this Assignment.
     * The set is backed by the map.
     * @see HashMap#keySet
     */
    public Set<RandomVariable> variableSet() {
	return keySet();
    }
    
    /** 
     * Returns a random variable key object directly if a matching one
     * exists in this Assignment (and null if it does not).
     */
    public RandomVariable getKeyByName(String name) {
    	for(RandomVariable key : this.keySet()) {
    		if(key.getName().equals(name)) {
    			return key;
    		}
    	}
    	return null;
    }
    
    /**
     * Updates RV references to BN nodes.
     * @param truth the BN list of variables
     */
    public void match(List<RandomVariable> truth) {
    	for(RandomVariable v : truth) {
    		RandomVariable mine = this.getKeyByName(v.getName());
    		if(mine != null) {
    			Object value = this.get(mine);
    			this.remove(mine);
    			this.put(v, value);
    		}
    	}
    }
    
    /*
     * Returns true if all pieces of the evidence are 
     * consistent with this assignment, false if not.
     */
    public boolean consistent(Assignment evidence) {
    	for(RandomVariable v : evidence.keySet()) {
    		if(!this.get(v).equals(evidence.get(v))) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /*
     * Extends this Assignment to include one more RandomVariable v 
     * with a given value.
     */
	public Assignment extend(RandomVariable v, Object value) {
		Assignment extended = this.copy();
		extended.put(v, value);
		return extended;
	}
	
    /**
     * Returns a shallow copy of this HashMap instance: the keys and
     * values themselves are not cloned.
     * @see LinkedHashMap#clone
     */
    public Assignment copy() {
	return (Assignment)this.clone();
    }

    public String toString() {
	StringBuilder buf = new StringBuilder();
	for (Map.Entry<RandomVariable,Object> entry : entrySet()) {
	    if (buf.length() > 0) {
		buf.append(",");
	    }
	    buf.append(entry.getKey());
	    buf.append("=");
	    buf.append(entry.getValue());
	}
	return buf.toString();
    }
}
