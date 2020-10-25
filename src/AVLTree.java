import java.util.Arrays;
import java.util.*;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {

  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */

	AVLNode root;
	private int cnt_rotations=0;
	private int cnt=0;
	private AVLNode suc;

  public boolean empty() {
	  return this.root==null; // to be replaced by student code
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	  AVLNode tmp=root;
		 if(tmp !=null) {
		  while (tmp.isRealNode()){
			  if (k == tmp.getKey()) {
				  return tmp.info;
			  }
			  else
			  {
				  if (k < tmp.getKey()) {
					  tmp=tmp.left;
				  }
				  else {
					  tmp = tmp .right;
				  }
			  }
		  	}
		 }
		return null;  // to be replaced by student code
  }


  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	   cnt_rotations=0;//**
	  if(root == null) {
		  root=new AVLNode(new AVLNode(),new AVLNode(), k,i);//
		  return 0;
	  }
	  if(search(k)!=null)
		  return -1;
	  AVLNode  tmp=root,x=null;

	  while(tmp.key != -1) { //where to insert
		  x=tmp;
		  if(tmp.getKey() > k)
			  tmp=tmp.left;
		  else
			  tmp=tmp.right;
	  }
	  AVLNode node= new AVLNode(new AVLNode(),new AVLNode(), k,i);//
	  if(k > x.getKey()) {
		  x.right=node;
		  node.parent=x;
	  }
	  else {
		  x.left=node;
		  node.parent=x;
	  }
	  updateSize(x);
	  updateSum(node);
	  if (x.left.key == -1 || x.right.key == -1) {//
		  updateHeight(x);
		  insertRebalance(x);
		}

	  return cnt_rotations;	// to be replaced by student code
   }

   /**
    * private void updateSum(AVLNode node)
    * update the sum of the nodes from node to root after the insertion
    */


   private void updateSum(AVLNode node) {
	   AVLNode tmp=node.parent;
	   int s=node.key;
	   while(tmp!=null) {
		   tmp.sum+=s;
		   tmp=tmp.parent;
	   }
   }
   /**
    * private void updateHeight(AVLNode node)
    * update the height of the tree nodes from node to root after the insertion
    */

   private void updateHeight(AVLNode node) {
	   if(node==null || Math.abs(node.left.height-node.right.height)==0 )
		   return;
	   node.height++;
	   updateHeight(node.parent);

   }

   /**
    *  private void updateSize(AVLNode node)
    * update the size of the tree nodes from node to root after the insertion
    */
   private void updateSize(AVLNode node) {

	   AVLNode tmp=node;
	   while(tmp!=null) {
		   tmp.Size++;
		   tmp=tmp.parent;
	   }

   }
   /**
    *  private int bF(AVLNode node)
    * @return the balance factor for this node
    * the difference between the height of the left son and right son
    */


   private int HeightSub(AVLNode node) {
	   if (!node.right.isRealNode() && node.left.isRealNode() ) {
			return node.height;
		}else if(!node.left.isRealNode() && node.right.isRealNode()){
			return -node.height ;
		}
       return (node.left.height - node.right.height);
	//    return node.left.height-node.right.height;

   }
   /***
    *  private void insertRebalance(AVLNode node)
    * Rebalancing the AVL tree after the insertion
    */
   private void insertRebalance(AVLNode node) {
	   AVLNode y=node;
	   int bf=HeightSub(y);
	  if(y==root)
		  return ;
	  while(y!= null) {
		  bf=HeightSub(y);
		  if(bf <2 && bf >-2)
			  y=y.parent;
		  else {
			  if(bf ==2 && HeightSub(y.left)==1) {
				  rightRotate(y.left,y.left.left);
				  cnt_rotations=1;
				  return;

			  }
			  if(bf ==2 && HeightSub(y.left)==-1) {
				AVLNode l=y.left;
				AVLNode lr=y.left.right;
				leftRotate(l,lr);
				rightRotate(lr,l);
				cnt_rotations=2;
				return;
		  }
			  if(bf ==-2 && HeightSub(y.right)==-1) {
				leftRotate(y.right, y.right.right);
				cnt_rotations=1;
				return;
			  }
			  if(bf ==-2 && HeightSub(y.right)==1) {
				  AVLNode r=y.right;
				  AVLNode rl=y.right.left;
				rightRotate(r,rl);
				leftRotate(rl,r);
				cnt_rotations=2;
			    return;
			  }
		  }
	  }
   }

   /***
    * private void leftRotate(AVLNode p, AVLNode child)
    * rotates the tree to the left
    */

   private void leftRotate(AVLNode p, AVLNode child) {
	   AVLNode grand=p.parent;
	   if (p==grand.left) {
		   grand.left=child;
		   child.parent=grand;
		   p.right=child.left;
		   child.left.parent=p;
		   child.left=p;
		   p.parent=child;
		   int s=p.Size;//
		   p.Size=p.left.Size+p.right.Size+1;
		   child.Size=s;
		   p.height=Math.max(p.left.height,p.right.height)+1;
		   child.sum=p.sum;
		   p.sum=p.left.sum+p.right.sum+p.key;
	   }
	   else {
		   AVLNode pL=p.left;
		   p.left=grand;
		    pL.parent=grand;
		   grand.right=pL;
		   if(grand.parent==null) {
			   root=p;
		   }
		   else {
			   if(grand==grand.parent.left)
				   grand.parent.left=p;
			   else
				   grand.parent.right=p;

		   }
		   AVLNode gB=grand.parent;
		   grand.parent=p;
		   p.parent=gB;
		   p.Size=grand.Size;
		   grand.Size=grand.left.Size+grand.right.Size+1;
		   grand.height=Math.max(grand.left.height,grand.right.height)+1;
		   p.height=Math.max(p.left.height,p.right.height)+1;
		   p.sum=grand.sum;
		   grand.sum= grand.left.sum+grand.key+pL.sum;
		   AVLNode tmp=p.parent;
		   while(tmp!=null) { //update height after rotation
			   tmp.height=Math.max(tmp.left.height, tmp.right.height)+1;
			   tmp=tmp.parent;
		   }
	   }
   }
   /***
    * private void rightRotate(AVLNode p,AVLNode child)
    * rotates the tree to the right
    */

   private void rightRotate(AVLNode p,AVLNode child) {
	   AVLNode grand=p.parent;

	   int s;
	   if(p==grand.right) {
		   grand.right=child;
		   child.parent=grand;
		   p.left=child.right;
		   child.right.parent=p;
		   child.right=p;
		   p.parent=child;
		   s=p.Size;
		   p.Size=p.left.Size+p.right.Size+1;
		   child.Size=s;
		   p.height=Math.max(p.left.height,p.right.height)+1;
		   child.sum=p.sum;
		   p.sum=p.left.sum+p.right.sum+p.key;

	   }
	   else {
		   AVLNode pR=p.right;
		   p.right=grand;
		   pR.parent=grand;
		   grand.left=pR;
		   if(grand.parent==null) {
			   root=p;
		   }
		   else {
			   if(grand==grand.parent.left)
				   grand.parent.left=p;
			   else
				   grand.parent.right=p;
		   }

		   p.parent=grand.parent;
		   grand.parent=p;
		   s=p.Size;//
		   p.Size=grand.Size;
		   grand.Size=grand.left.Size+grand.right.Size+1;
		   grand.height=Math.max(grand.left.height,grand.right.height)+1;
		   p.height=Math.max(p.left.height,p.right.height)+1;
		   p.sum=grand.sum;
		   grand.sum=grand.right.sum+grand.key+grand.left.sum;
		   AVLNode tmp=p.parent;
		   while(tmp!=null) { //update height after rotation
			   tmp.height=Math.max(tmp.left.height, tmp.right.height)+1;
			   tmp=tmp.parent;
		   }
	   }
   }
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   int flag=0;
	   int c=0;
	   AVLNode node = find(k);
	   if(node == null)
		   return -1;
	   if (node == root && node.Size==1) {
		   root=null;
		   return 0;
	   }
	   if(node == root && !node.left.isRealNode()) {
		   root=root.right;
		   root.parent=null;
		   return 0;
	   }
	   if(node == root && !node.right.isRealNode()) {
		   root=root.left;
		   root.parent=null;
		   return 0;
	   }
	   if((node.left.isRealNode()) && (node.right.isRealNode()) ){// node has left and right sons
		   decreaseSum(node);
		   successor(node);// replace the node with the successor
		   updateSumsuc(node,suc);
		   decreaseSize(node.parent);
		   suc.sum=suc.sum-node.key;
		   flag=1;

	   }
	   if(flag==0) {
		   decreaseSum(node);
		   decreaseSize(node.parent);
	   }
        if((!node.left.isRealNode()) && (!node.right.isRealNode())) {// node is leaf
        	AVLNode x= node.parent;
		   if (node.parent.right==node) {// node is right son
			   x.right=new AVLNode();
			   if(node.parent.left.isRealNode()) {
				   if(HeightSub(node.parent) <2 && HeightSub(node.parent) >-2 )
					   c=0;
				   else
					   c+=deleteRebalance(x);
			   }
			   else {
				   decreaseHeight(x);
				   c+=deleteRebalance(x);
			   }
		   }
		   else {// node is left son
			   x.left=new AVLNode();
			if(node.parent.right.isRealNode()) {
				if(HeightSub(node.parent) <2 && HeightSub(node.parent) >-2 ) {
					   c=0;
				}
				   else {
					   c+=deleteRebalance(x);
				   }
			   }
			   else {
				   decreaseHeight(x);
				   c+=deleteRebalance(x);
			   }
		   }
        }
        if((node.left.isRealNode()) && (!node.right.isRealNode()) && (node.parent.left==node)) {
        	AVLNode x= node.parent;
        AVLNode tmp = node.left;
        x.left=tmp;
        tmp.parent=x;
        node.left=new AVLNode();
        decreaseHeight(x);
		c+=deleteRebalance(x);

        }
        if((!node.left.isRealNode()) && (node.right.isRealNode())&& ( node.parent.left==node)) {
        	AVLNode x= node.parent;
            AVLNode tmp = node.right;
            x.left=tmp;
            tmp.parent=x;
            node.right=new AVLNode();
            decreaseHeight(x);
    		c+=deleteRebalance(x);
        }
        if((!node.left.isRealNode()) && (node.right.isRealNode())&& ( node.parent.right==node)) {
            AVLNode x= node.parent;
            AVLNode tmp = node.right;
            x.right=tmp;
            tmp.parent=x;
            node.right=new AVLNode();
            decreaseHeight(x);
    		c+=deleteRebalance(x);
        }
        if((node.left.isRealNode()) && (!node.right.isRealNode())&& ( node.parent.right==node)) {
            AVLNode x= node.parent;
            AVLNode tmp = node.left;
            x.right=tmp;
            tmp.parent=x;
            node.left=new AVLNode();
            decreaseHeight(x);
    		c+=deleteRebalance(x);
        }
        node.parent=null;

	   return c;		// to be replaced by student code
   }

   /**
    * private void decreaseSum(AVLNode node)
    * decrease the sum of the nodes from node to root after the deletion
    */

   private void decreaseSum(AVLNode node) {
	   AVLNode tmp=node.parent;
	   int s=node.key;
	   while(tmp!=null) {
		   tmp.sum-=s;
		   tmp=tmp.parent;
	   }
   }

   /**
    * private void updateSumsuc(AVLNode node,AVLNode successor)
    * update the sum of the nodes from node to the successor after replacing between them
    *
    */
   private void updateSumsuc(AVLNode node,AVLNode successor) {
	   AVLNode tmp=node.parent;
	   int s=successor.key;
	   while(tmp!=successor) {
		   tmp.sum-=s;
		   tmp=tmp.parent;
	   }
   }

   /**
    * private int deleteRebalance(AVLNode node)
    * rebalancing the AVL tree after the deletion
    * returns the number of rebalancing operations
    *
    */
   private int deleteRebalance(AVLNode node) {
	   int count=0;
	   AVLNode y=node,x=node;
	   int bf=HeightSub(y);
	  while(y!= null) {
		  bf=HeightSub(y);
		  if(bf <2 && bf >-2) {
			  y=y.parent;
			  x=y;
		  }
		  else {
			  if((bf ==2) && (HeightSub(y.left)==1 || HeightSub(y.left)==0)) {
				  rightRotate(x.left,x.left.left);
				  count++;
			  }
			  else
			  if((bf ==2 ) && (HeightSub(y.left)==-1) ){
			     AVLNode l=x.left;
			     AVLNode lr=x.left.right;
			     leftRotate(l,lr);
			     rightRotate(lr,l);
				 count+=2;
			  }
			  else
			  if((bf ==-2) &&(HeightSub(y.right)==-1 || HeightSub(y.right)==0)){
				 leftRotate(x.right,x.right.right);
				 count ++;
			  }
			  else
			  if((bf ==-2 ) && (HeightSub(y.right)==1)){
				AVLNode  r=x.right;
				AVLNode rl=x.right.left;
			    rightRotate(r,rl);
			    leftRotate(rl,r);
			    count+=2;
			  }

		  y=y.parent;
		  x=y;
		  }
	  }
	  return count;
   }


   /**
   private void successor(AVLNode node)
  *
  *replaces the node with it's successor.
  */

   private void successor(AVLNode node) { /*this deals only with node with right children
	    * because, otherwise, node is a leaf or with only left children, and will be dealt with in other ways.
	    */
	   suc=node.right;
	   while(suc.left.key!=-1) {
		   suc=suc.left;
	   }
	   AVLNode y=suc.parent;
	   if(y!=node) {
		   suc.parent=node.parent;
		   y.left=node;
		   node.parent=y;
		   if (suc.parent!=null) {
			   if(suc.parent.left==node)
				   suc.parent.left=suc;
			   else
				   suc.parent.right=suc;
		   }
		   else
			   root=suc;
		   node.right.parent=suc;
		   node.left.parent=suc;
		   if (suc.right.isRealNode()) {
			   suc.right.parent=node;
		   }

		   y=node.right;
		   node.right=suc.right;
		   suc.right=y;
		   suc.left=node.left;
		   node.left=new AVLNode();
		   int s=node.Size;
		   node.Size=suc.Size;
		   suc.Size=s;
		   int h=node.height;
		   node.height=suc.height;
		   suc.height=h;
		   s = node.sum;
		   node.sum=suc.sum;
		   suc.sum=s;
	   }
	   else {
		   suc.parent=node.parent;

		  if (node.parent!=null) {
			  if(suc.parent.left==node) {
				   suc.parent.left=suc;
			   }
			  else {
				   suc.parent.right=suc;
			   }
		  }
		  else {
			 root=suc;
		  }
		   suc.left=node.left;
		   suc.left.parent=suc;
		   node.left=new AVLNode();
		   node.right=suc.right;
		   if(node.right.isRealNode())
			   node.right.parent=node;
		   suc.right=node;
		   node.parent=suc;
		   int s=node.Size;
		   node.Size=suc.Size;
		   suc.Size=s;
		   int h=node.height;
		   node.height=suc.height;
		   suc.height=h;
		   s = node.sum;
		   node.sum=suc.sum;
		   suc.sum=s;

	   }
   }
   /**
    * private void decreaseSize(AVLNode node)
    * decrease the size of the nodes from node to root after the deletion
    */
   private void decreaseSize(AVLNode node) {
	   	AVLNode tmp=node;
	   	while(tmp!=null) {
	   		tmp.Size--;
	   		tmp=tmp.parent;
	   	}
   }
   /**
    * private void decreaseHeight(AVLNode node)
    * update the height of the nodes from node to root after the deletion
    */
	   private void decreaseHeight(AVLNode node) {
		   if(node==null || Math.abs(node.left.height-node.right.height)!=0 )
			   return;
		   node.height--;
		   decreaseHeight(node.parent);
	}

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   if(root==null)
		   return null;
	   AVLNode tmp=root;
	   while (tmp.left.key!=-1) {
		   tmp =tmp.left;
	   }
	   return tmp.info; // to be replaced by student code
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if(root == null)
		   return null;
	   AVLNode tmp=root;
	   while (tmp.right.key!=-1) {
		    tmp =tmp.right;
	   }
	   return tmp.info; // to be replaced by student code
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  if(this.empty())
		  return new int[0];
	  int[] arr = new int[root.getSubtreeSize()];
	  this.cnt=0;
	  inOrderscan(root,arr);
	   return arr; // to be replaced by student code
                     // to be replaced by student code
  }
  /**
   * private void inOrderscan(AVLNode node,int [] result)
   * change the array "result" to sorted array that contains the keys
   */
  private void inOrderscan(AVLNode node,int [] result) {
	   if(node.key!=-1) {
		   inOrderscan(node.left,result);
		   result[cnt]=node.key;
		   cnt++;
		   inOrderscan(node.right,result);
	   }
}

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  if(this.empty())
		  return new String[0];
	  String[] arr = new String[root.getSubtreeSize()];
	  this.cnt=0;

	  inOrderscanInfo(root,arr);
	   return arr;                    // to be replaced by student code
  }
  /**
   * private void inOrderscan(AVLNode node,int [] result)
   * change the array "result" to sorted array that contains the info
   */
  private void inOrderscanInfo(AVLNode node,String [] result) {
	   if(node.isRealNode()) {
		   inOrderscanInfo(node.left,result);
		   result[cnt]=node.info;
		   cnt++;
		   inOrderscanInfo(node.right,result);
	   }
}

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   if(this.empty())
		   return 0;
	   return root.getSubtreeSize();  // to be replaced by student code
   }

     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   {
	   return root;
   }
     /**
    * public string split(int x)
    *
    * splits the tree into 2 trees according to the key x.
    * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	  * precondition: search(x) != null
    * postcondition: none
    */
   public AVLTree[] split(int x)
   {
	   IAVLNode temp = find(x);
	   AVLTree leftTree = new AVLTree() , rightTree= new AVLTree();
	   if(!temp.getLeft().isRealNode())
		   leftTree = new AVLTree();
	   else {
		   leftTree.root = preOrderCopy((AVLNode) temp.getLeft());
		   leftTree.root.height = temp.getLeft().getHeight();
		   leftTree.root.Size = temp.getLeft().getSubtreeSize();
	   }
	   if(!temp.getRight().isRealNode())
		   rightTree =new AVLTree();
	   else {
		   rightTree.root = preOrderCopy((AVLNode) temp.getRight());
		   rightTree.root.height = temp.getRight().getHeight();
		   rightTree.root.Size = temp.getRight().getSubtreeSize();
	   }
	   try {
	   while (temp != root){
		   if(temp.getParent().getLeft().equals(temp)) {
			   temp=temp.getParent();
			   IAVLNode temp1 =new AVLNode(new AVLNode(),new AVLNode(),temp.getKey(),temp.getValue());
			   AVLTree temptree = new AVLTree();
			   temptree.root=preOrderCopy((AVLNode) temp.getRight());
			   if(!temptree.root.isRealNode())
				   temptree = new AVLTree();
			   rightTree.join(temp1, temptree);

		   }
		   else {
			   temp = temp.getParent();
			   IAVLNode temp1 =new AVLNode(new AVLNode(),new AVLNode(),temp.getKey(),temp.getValue());
			   AVLTree temptree = new AVLTree();
			   temptree.root=preOrderCopy((AVLNode) temp.getLeft());

			   if(!temptree.root.isRealNode())
				   temptree = new AVLTree();
			   leftTree.join(temp1, temptree);

		   }


	   }
	   }
	   catch (Exception e) {
	   }
	   AVLTree[] arr = new AVLTree[2];
	   arr[0] = leftTree;
	   arr[1] = rightTree;
	   return arr;

   }


   private AVLNode preOrderCopy(AVLNode focusNode) {
       if (focusNode == null) {
           // base case
           return null;
       }
       AVLNode copy = new AVLNode(focusNode.right,focusNode.left,focusNode.key, focusNode.info);
       copy.setLeft(preOrderCopy((AVLNode) focusNode.getLeft()));
       copy.setRight(preOrderCopy((AVLNode) focusNode.getRight()));
       return copy;
   }



   /**
    *  public void updateHeight2( AVLNode x )
    *  updates the height of the nodes after joining them
    *  from x to root.
    * @param x
    */

   public void updateHeight2( AVLNode x )
   {
      while ( x != null )
      {
         x.height = Math.max(x.left.height, x.right.height ) + 1 ;   // Compute height for x
         x = x.parent;              // Go to the parent node
      }

   }
   /**
    * public void upsize(AVLNode r)
    * updates the size of the nodes after joining them,
    * from node x to root
    * @param r
    */
   public void upsize(AVLNode r) {
	   while (r != null) {
		   r.Size = r.left.Size + r.right.Size + 1;
		   r = r.parent;
	   }
   }

   /**
    * public join(IAVLNode x, AVLTree t)
    *
    * joins t and x with the tree.
    * Returns the complexity of the operation (rank difference between the tree and t + 1)
	* precondition: keys(x,t) < keys() or keys(x,t) > keys()
    * postcondition: none
    *
    */

   public int join(IAVLNode x, AVLTree t)
   {
	   IAVLNode a , b;
	   int size,k;
	   if(this.root==null) {
		   t.insert(x.getKey(), x.getValue());
		   root=t.root;
		   return t.root.getHeight()+1;
	   }
	   if(t.root ==null) {
		   this.insert(x.getKey(), x.getValue());
		   return root.getHeight()+1;
	   }
	   int s1=root.getSubtreeSize();
	   int s2 = t.size();
	   if((Math.abs(t.getRoot().getHeight() - root.getHeight())) <= 1) {
		   if(t.getRoot().getKey() > root.getKey()) {
			   x.setLeft(root);
			   x.setRight(t.getRoot());
			   updateHeight2((AVLNode)x);
			   this.root= (AVLNode) x;
			   t.root = (AVLNode) x; //
			   x.setSubtreeSize(s1+s2+1);
			   return 1;
		   }
		   else {
			   x.setLeft(t.getRoot());
			   x.setRight(root);
			   updateHeight2((AVLNode)x);
			   this.root= (AVLNode) x;
			   t.root = (AVLNode) x; //
			   x.setSubtreeSize(s1+s2+1);
			   return 1;

		   }
	   }
	   int d= Math.abs(t.getRoot().getHeight() - root.getHeight());
	   if (t.getRoot().getHeight() > root.getHeight()) {
		   a = t.getRoot();
		   if(t.getRoot().getKey() > root.getKey()) {
			   while (a.getHeight() > root.getHeight()) {
				   a = a.getLeft();
			   }
			   size = a.getSubtreeSize();
			   b = a.getParent();
			   x.setLeft(root);
			   x.setRight(a);
			   x.setParent(b);
			   b.setLeft(x);
			   updateHeight2((AVLNode) x);
			   x.setSubtreeSize(size+s1+1);
			   root = (AVLNode) t.getRoot();
			   upsize((AVLNode)x.getParent());
			   insertRebalance((AVLNode)x.getParent());

			   return (d+1);
		   }
		   else {
			   while (a.getHeight() > root.getHeight()) {
				   a = a.getRight();
			   }
			   size = a.getSubtreeSize();
				   b = a.getParent();
				   x.setLeft(a);
				   x.setRight(root);
				   x.setParent(b);
				   b.setRight(x);
				   updateHeight2((AVLNode) x);
				   root = (AVLNode) t.getRoot();
				   x.setSubtreeSize(size+s1+1);
				   upsize((AVLNode)x.getParent());
				   insertRebalance((AVLNode)x.getParent());
				   return (d+1);

			   }
		   }
	   else {
		   a=root;
		   if(t.getRoot().getKey() < root.getKey()) {
			   while (a.getHeight() > t.getRoot().getHeight()) {
				   a = a.getLeft();
			   }

			   size = a.getSubtreeSize();
			   b=a.getParent();
			   x.setLeft(t.getRoot());
			   x.setRight(a);
			   x.setParent(b);
			   b.setLeft(x);
			   updateHeight2((AVLNode) x);
			   x.setSubtreeSize(size+s2+1);
			   upsize((AVLNode)x.getParent());
			   insertRebalance((AVLNode)x.getParent());

			   return (d+1);

		   }
		   else {
			   while (a.getHeight() > t.getRoot().getHeight()) {
				   a = a.getRight();
			   }

			   size = a.getSubtreeSize();
			   b=a.getParent();
			   x.setLeft(a);
			   x.setRight(t.getRoot());
			   x.setParent(b);
			   b.setRight(x);
			   updateHeight2((AVLNode) x);
			   x.setSubtreeSize(size+s2+1);
			   upsize((AVLNode)x.getParent());
			   insertRebalance((AVLNode)x.getParent());
			   return (d+1);

		   }
	   }

   }


   /**
    * private AVLNode find(int k)
    * returns the node of an item with key k if it exists in the tree
    * otherwise, returns null
    */
   private AVLNode find(int k) {
	   AVLNode tmp=root;
	   if(tmp !=null) {
		  while (tmp.isRealNode()){
			  if (k == tmp.getKey())
				  return tmp;
			  else
			  {
				  if (k < tmp.getKey())
					  tmp=tmp.left;
				  else
					  tmp = tmp.right;
			  }
		  }
	   }
	   return null;
   }


	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    	public void setSubtreeSize(int size); // sets the number of real nodes in this node's subtree
	    public int getSubtreeSize();  // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))


	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
	public class AVLNode implements IAVLNode{
		AVLNode right;
		AVLNode left;
		AVLNode parent;
		int key;
		String info;
		int Size;
		int sum;
		int height;

		public AVLNode() {
			this.key=-1;
			this.Size=0;
			this.height=-1;
			this.left=null;
			this.right=null;
		}
		public AVLNode(AVLNode node) {
			this.key=-1;
			this.Size=0;
			this.height=-1;
			this.parent=node;
			this.left=null;
			this.right=null;


		}

		public AVLNode(AVLNode right, AVLNode left,int key, String info) {
			this.right=right;
			this.left=left;
			this.key=key;
			this.info=info;
			this.Size=1;
			this.height=0;
			if (this.left==null)
				this.left=new AVLNode(this);
			if(this.right==null)
				this.right=new AVLNode(this);
			if (!this.left.isRealNode())
				this.left=new AVLNode(this);
			if (!this.right.isRealNode())
				this.right=new AVLNode(this);

			this.parent=null;
			this.sum=key;
		}
		public int getKey()
		{
			return this.key;  // to be replaced by student code
		}
		public String getValue()
		{
			return this.info; // to be replaced by student code
		}
		public void setLeft(IAVLNode node)
		{
			this.left=(AVLNode) node;  // to be replaced by student code
		}
		public IAVLNode getLeft()
		{
			return this.left; // to be replaced by student code
		}
		public void setRight(IAVLNode node)
		{
			this.right=(AVLNode) node; // to be replaced by student code
		}
		public IAVLNode getRight()
		{
			return this.right; // to be replaced by student code
		}
		public void setParent(IAVLNode node)
		{
			this.parent=(AVLNode) node ; // to be replaced by student code
		}
		public IAVLNode getParent()
		{
			return this.parent;// to be replaced by student code
		}
		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode()
		{
			return this.getKey() !=-1;  // to be replaced by student code
		}
		public void setHeight(int height)
		{
			this.height=height; // to be replaced by student code
		}
		public int getHeight()
		{
			return this.height; // to be replaced by student code
		}

		public void setSubtreeSize(int size)
		{
			this.Size=size;
		}
		public int getSubtreeSize()
		{
			return this.Size;
		}
		public int getSubtreeSum()
	  	{
	  		return this.sum;
	  	}
	}



}


