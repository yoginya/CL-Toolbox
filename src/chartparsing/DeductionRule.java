package chartparsing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Item;

public class DeductionRule {
	Set<Item> antecedences = new HashSet<Item>();
	Set<Item> consequences = new HashSet<Item>();
	String name = null;
	boolean generate = false;

	public void addAntecedence(Item item) {
		antecedences.add(item);
	}

	public void addConsequence(Item item) {
		consequences.add(item);
	}

	public Set<Item> getAntecedences() {
		return antecedences;
	}

	public void setAntecedences(Set<Item> antecedences) {
		this.antecedences = antecedences;
	}

	public Set<Item> getConsequences() {
		return consequences;
	}

	public void setConsequences(Set<Item> consequences) {
		this.consequences = consequences;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	public String getName (){
		return this.name;
	}
	
	@Override
	public String toString() {
		StringBuilder representation = new StringBuilder();
		for (Item rule : antecedences){
			representation.append(rule.toString());
		}
		representation.append("\n______\n");
		for (Item rule : consequences){
			representation.append(rule.toString());
		}
		return representation.toString();
	}
}