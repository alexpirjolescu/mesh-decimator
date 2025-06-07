/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplify;
//import java.util.*;
//import mesh.*;
//import simplify.*;
/**
 *
 * @author alexp
 */

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MeshSimplifier {
    // minheap of collapse cost 
        // should be a PriotityQueue<EdgeCollapse> using cost as primary key
    private PriorityQueue<EdgeCollapse> heap = new PriorityQueue<>(Comparator.comparingDouble(e-> e.cost));
    // add version to each vertex and increment version of new vertex created 
    // find collapse position 
    // create new vertex with optimal position
    // create edge and face of which new vertex is part of 
            // remove degenerate faces (faces where 2 indices are the same)
            //add isDegenerate() method

        // remove old vertices (v1 and v2) from the 
    // insert the new vertex and add conncetions (edge and face)
    // update edge collapse candidate list 
    
}
