package kdtree;

import java.util.ArrayList;

public class KdTree<Point extends PointI>
{
	/** A node in the KdTree
	 */
	public class KdNode 
	{
		KdNode child_left_, child_right_;
		Point pos_;
		int d_; 	/// dimension in which the cut occurs
		
		KdNode(Point p, int d){
			this.pos_ = p;
			this.d_ = d;
			this.child_left_ = null;
			this.child_right_ = null;
		}

		KdNode(Point p, int d, KdNode l_child, KdNode r_child){
			this.pos_ = p;
			this.d_ = d;
			this.child_left_ = l_child;
			this.child_right_ = r_child;
		}
		
		/** 
		 * if strictly negative the query point is in the left tree
		 * TODO: equality is problematic if we want a truly balanced tree
		 */
		int dist1D(Point p) { 
			return p.get(d_) - pos_.get(d_);
		}
	}
	
	/////////////////
    /// Attributs ///
    /////////////////

	private final int dim_; /// dimension of space
	private int n_points_; /// number of points in the KdTree
	
	private KdNode root_; /// root node of the KdTree

    //////////////////
    /// Constructor///
    //////////////////

	/** Initialize an empty kd-tree
	 */
	public KdTree(int dim) {
		this.dim_ = dim;
		this.root_ = null;
		this.n_points_ = 0;
	}

	/** Initialize the kd-tree from the input point set
	 *  The input dimension should match the one of the points
	 */
	public KdTree(int dim, ArrayList<Point> points, int max_depth) {
		this.dim_ = dim;
		this.n_points_ = points.size();
		
		this.root_ = theTree(points, 0, max_depth);
	
	}
	  
	/////////////////
	/// Accessors ///
	/////////////////

	int dimension() { return dim_; }

	int nb_points() { return n_points_; }

	void getPointsFromLeaf(ArrayList<Point> points) {
		getPointsFromLeaf(root_, points);
	}

	 
	///////////////
	/// Mutator ///
	///////////////

	/** Insert a new point in the KdTree.
	 */
	void insert(Point p) {
		if(this.contains(p)){
		}
		else{
			n_points_ += 1;
			
			if(root_==null) 
				root_ = new KdNode(p, 0);
				
			KdNode node = getParent(p);
			if(node.dist1D(p)<0) {
				assert(node.child_left_==null);
				node.child_left_ = new KdNode(p, (node.d_+1)%dim_);
			} else{
				assert(node.child_right_==null);
				node.child_right_ = new KdNode(p, (node.d_+1)%dim_);
			}
		}
	}
	
	KdNode theTree(ArrayList<Point> points, int current_depth, int max_depth){		
		int dim = rightDim(points);
		
		if(points.isEmpty()){
			return null;
		}
		if(points.size()==1 || current_depth>=max_depth){
			return new KdNode(barycentre(points), dim);
		}
		else{
			points.sort( (Point a, Point b) -> a.get(dim) - b.get(dim) );
			KdNode node = chosenNode(points, dim);
			node.child_left_ = theTree(leftNodes(points), current_depth+1, max_depth);
			node.child_right_ = theTree(rightNodes(points), current_depth+1, max_depth);
			return node;
		}
	}

	void delete(Point p) {
		assert(false);
	}

	///////////////////////
	/// Query Functions ///
	///////////////////////

	/** Return the node that would be the parent of p if it has to be inserted in the tree
	 */
	KdNode getParent(Point p) {
		assert(p!=null);
		
		KdNode next = root_, node = null;

		while (next != null) {
			node = next;
			if ( node.dist1D(p) < 0 ){
				next = node.child_left_;
			} else {
				next = node.child_right_;
			}
		}
		
		return node;
	}
	
	/** Check if p is a point registered in the tree
	 */
	boolean contains(Point p) {
        return contains(root_, p);
	}

	/** Get the nearest neighbor of point p
	 */
    public Point getNN(Point p)
    {
    	assert(root_!=null);
        return getNN(root_, p, root_.pos_);
    }

	///////////////////////
	/// Helper Function ///
	///////////////////////

    /** Add the points in the leaf nodes of the subrre defined by root 'node'
     * to the array 'point'
     */
	private void getPointsFromLeaf(KdNode node, ArrayList<Point> points)
	{
		if(node.child_left_==null && node.child_right_==null) {
			points.add(node.pos_);
		} else {
		    if(node.child_left_!=null)
		    	getPointsFromLeaf(node.child_left_, points);
		    if(node.child_right_!=null)
		    	getPointsFromLeaf(node.child_right_, points);
		}
	 }
	
	/** Search for a better solution than the candidate in the subtree with root 'node'
	 *  if no better solution is found, return candidate
	 */
	 private Point getNN(KdNode node, Point point, Point candidate)
	 {
	    if ( point.sqrDist(node.pos_) <  point.sqrDist(candidate)) 
	    	candidate = node.pos_;

	    int dist_1D = node.dist1D(point);
	    KdNode n1, n2;
	    if( dist_1D < 0 ) {
	    	n1 = node.child_left_;
	    	n2 = node.child_right_;
	    } else{
	    	// start by the right node
	    	n1 = node.child_right_;
	    	n2 = node.child_left_;
	    }

	    if(n1!=null)
	    	candidate = getNN(n1, point, candidate);

	    if(n2!=null && dist_1D*dist_1D < point.sqrDist(candidate)) 
	    	candidate = getNN(n2, point, candidate);
		 
		 return candidate;
	 }
	 
	private boolean contains(KdNode node, Point p) {
        if (node == null) return false;
        if (p.equals(node.pos_)) return true;

        //TODO : assume the "property" is strictly verified
        if (node.dist1D(p)<0)
            return contains(node.child_left_, p);
        else
            return contains(node.child_right_, p);
	}
	
	private ArrayList<Point> leftNodes(ArrayList<Point> points) {
		return new ArrayList<Point> (points.subList(0, points.size() /2));
	}

	private ArrayList<Point> rightNodes(ArrayList<Point> points) {
		return new ArrayList<Point> (points.subList((points.size()/2)+1,points.size()));
	}

	private KdNode chosenNode(ArrayList<Point> points, int dim) {
		return new KdNode(points.get(points.size()/2),dim);
	}

	private Point barycentre(ArrayList<Point> points) {
		Point b = points.remove(0);
		for(Point p: points)
			b.add(p);
		b.div(points.size()+1);
		return b;
	}

	private int rightDim(ArrayList<Point> points) {
		int bestDim = 0;
		float var = variance (points, bestDim);
		for(int i=0; i<this.dim_;i++){
			if(var<variance(points,i)){
				var=variance(points,i);
				bestDim = i;
			}
		}
		return bestDim;
	}

	private float variance(ArrayList<Point> points, int bestDim) {
		float m = moyenne(points, bestDim);
		float sum = 0;
		for(Point p : points){
			sum += (p.get(bestDim) - m) * (p.get(bestDim) - m);
		}
		return sum / points.size();
	}

	private float moyenne(ArrayList<Point> points, int bestDim) {
		float sum = 0;
		for(Point p : points){
			sum += p.get(bestDim);
		}
		return sum / points.size();
	}
		
}


