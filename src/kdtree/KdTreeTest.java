package kdtree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

public class KdTreeTest {

	ArrayList<Point2i> grid() {
		ArrayList<Point2i> v_tree_points = new ArrayList<Point2i>();
		for(int i=0; i<10; ++i) {
			for(int j=0; j<10; ++j) {
				v_tree_points.add(new Point2i(10*i,10*j));
			}
		}
		return v_tree_points;
	}
	
	@Test
	public void testNearestNeighbor() {

		// Init vector of points

		ArrayList<Point2i> v_tree_points = grid();
		
		// Create kd-tree
		
		KdTree<Point2i> tree = new KdTree<Point2i>(2, v_tree_points,Integer.MAX_VALUE);
		
		// Create a vector of query Point
		ArrayList<Point2i> v_query_points = new ArrayList<Point2i>();
		v_query_points.add(new Point2i(0,0));
		v_query_points.add(new Point2i(100,100));
		v_query_points.add(new Point2i(200,200));
		v_query_points.add(new Point2i(25,150));
		v_query_points.add(new Point2i(25,55));
		v_query_points.add(new Point2i(33,25));
		
		// Compare result of linear search with kdtree search
		

		for (Point2i p : v_query_points) {

			//TODO: move that in linear search class
			float l_min = Float.MAX_VALUE;
	        for (Point2i pi: v_tree_points) {
	        	float sqr_dist = pi.sqrDist(p);
	        	if(sqr_dist < l_min) {
	        		l_min = sqr_dist;
	        	}
	        }	        	
	        System.out.println(l_min);
	        Point2i np = tree.getNN(p);
	        float t_min = p.sqrDist(np);
	        System.out.println(t_min);
		    assertTrue(t_min==l_min);
		}
	}
	
	@Test
	public void testNombrePoints() {
		ArrayList<Point2i> v_tree_points = grid();
		KdTree<Point2i> tree = new KdTree<Point2i>(2, v_tree_points,Integer.MAX_VALUE);
		assertEquals(v_tree_points.size(),tree.nb_points());
		tree.insert(new Point2i(5,4));
		tree.insert(new Point2i(4,4));
		tree.insert(new Point2i(6,12));
		tree.insert(new Point2i(5,1));
		assertEquals(v_tree_points.size()+4,tree.nb_points());
		tree.insert(new Point2i(0,0));
		assertEquals(v_tree_points.size()+4,tree.nb_points());
		}
	
	public void testContains() {
		ArrayList<Point2i> v_tree_points = grid();
		KdTree<Point2i> tree = new KdTree<Point2i>(2, v_tree_points,Integer.MAX_VALUE);
		Point2i p1=new Point2i(5,4);
		Point2i p2=new Point2i(4,4);
		Point2i p3=new Point2i(6,12);
		tree.insert(p1);
		tree.insert(p2);
		tree.insert(p3);
		tree.contains(p1);
		tree.contains(p2);
		tree.contains(p3);
	}
	
	
	
}
