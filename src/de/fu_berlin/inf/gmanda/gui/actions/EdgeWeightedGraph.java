package de.fu_berlin.inf.gmanda.gui.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.fu_berlin.inf.gmanda.util.CMultimap;

public class EdgeWeightedGraph {

	public static class V {
		
		HashSet<E> edges;
		
		public double getDegree(){
			
			double degree = 0.0;
			for (E e : edges){
				degree += e.weight;
			}
			
			return degree;
		}
		
	}
	
	public static class E {
		
		V v1, v2;
		
		double weight;
		
		
	}
	
	HashSet<E> edges;
	
	HashSet<V> vertices;
	
	public double getM(){
		
		double edgeWeight = 0.0;
		
		for (E e : edges){
			edgeWeight += e.weight;
		}
		return edgeWeight / 2.0;
	}
	
	public HashMap<V, Set<V>> getCommunities(){
		HashMap<V, Set<V>> result = new HashMap<V, Set<V>>();
		
		for (V v : vertices){
			Set<V> set = new HashSet<V>();
			set.add(v);
			result.put(v, set);
		}
		
		for (E e : edges){
			Set<V> v1 = result.get(e.v1);
			Set<V> v2 = result.get(e.v2);
			if (v1 != v2){
				result.put(e.v2, v1);
				v1.addAll(v2);
			}
		}
		return result;
	}
	
	public double getModularity(){
		
		double twoM = 2.0 * getM();
		HashMap<V, Set<V>> communities = getCommunities();
		double q = 0.0;
		
		for (E e: edges){
			if (communities.get(e.v1) != communities.get(e.v2))
				continue;
			
			q += e.weight; 
		}
		
		HashSet<Set<V>> collapsedCommunities = new HashSet<Set<V>>();
		collapsedCommunities.addAll(communities.values());
		for (Set<V> community : collapsedCommunities){
			for (V v1 : community){
				for (V v2 : community){
					q -= v1.getDegree() * v2.getDegree() / twoM;
				}
			}
		}
		return q / twoM;
	}
	
	public void shortestPath(Set<V> visited, Collection<E> pathToHere, V next){
		
		
		
	}

	public CMultimap<Double, E> edgeBetweenness() {
		return null;
	}
	
	
	
	
}
