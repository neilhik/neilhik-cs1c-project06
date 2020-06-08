package lazyTrees;

import java.util.NoSuchElementException;

public class LazySearchTree<E extends Comparable< ? super E >>
        implements Cloneable
{
    protected int mSize;
    protected LazySTNode mRoot;
    protected int mSizeHard;

    public LazySearchTree()
    {
        clear();
    }

    public boolean empty()
    {
        return (mSize == 0);
    }

    public int size()
    {
        return mSize;
    }

    public int sizeHard() {
        return mSizeHard;
    }

    public void clear()
    {
        mSize = 0;
        mRoot = null;
    }

    public E findMin()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return (E) findMin(mRoot).data;
    }

    protected E findMinHard(){
        if (mRoot == null)
            throw new NoSuchElementException();
        return (E) findMin(mRoot).data;
    }
    protected E findMaxHard(){
        if (mRoot == null)
            throw new NoSuchElementException();
        return (E) findMax(mRoot).data;
    }


    public E findMax()
    {
        if (mRoot == null)
            throw new NoSuchElementException();
        return (E) findMax(mRoot).data;
    }


    public E find( E x )
    {
        LazySTNode<E> resultNode;
        resultNode = find(mRoot, x);
        if (resultNode == null)
            throw new NoSuchElementException();
        return resultNode.data;
    }

    public boolean contains(E x)
    { return find(mRoot, x) != null; }

    public boolean insert( E x )
    {
        int oldSize = mSize;
        mRoot = insert(mRoot, x);
        return (mSize != oldSize);
    }

    public boolean remove( E x )
    {
        int oldSize = mSize;
        remove(mRoot, x);
        return (mSize != oldSize);
    }
    public boolean collectGarbage(){
        if (mSizeHard == 0 && mRoot == null)
            return false;
        collectGarbage(mRoot);
        return true;
    }
    private LazySTNode<E>  collectGarbage(LazySTNode<E> root) {
        if (root == null)
            return null;

        LazySTNode<E> t = collectGarbage(root.lftChild);
        if (root.deleted)
            removeHard(root, root.data);
        LazySTNode<E> t2 = collectGarbage(root.rtChild);
        return t2;
    }
    protected LazySTNode<E> removeHard(LazySTNode<E> root, E x){
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
            root.lftChild = removeHard(root.lftChild, x);
        else if ( compareResult > 0 )
            root.rtChild = removeHard(root.rtChild, x);

            // found the node
        else if (root.lftChild != null && root.rtChild != null)
        {
            root.data = findMin(root.rtChild).data;
            root.rtChild = removeHard(root.rtChild, root.data);
        }
        else
        {
            root =
                    (root.lftChild != null)? root.lftChild : root.rtChild;
            mSize--;
        }
        return root;
    }
    public<F extends Printable<E>> void traverseHard(F func) {

        traverseHard(func, mRoot);

    }
    protected<F extends Printable<E>> void traverseHard(F func, LazySTNode treeNode) {
        if (treeNode == null)
            return;


        traverseHard(func, treeNode.lftChild);
        func.print((E) treeNode.data);
        traverseHard(func, treeNode.rtChild);
    }
    protected <F extends Printable<E>> void traverseSoft(F func) {
        traverseSoft(func, mRoot);
        if (mSize == 0)
            System.out.println("Empty");
    }
    protected<F extends Printable<E>> void traverseSoft(F func, LazySTNode treeNode) {
        if (treeNode == null)
            return;
        traverseSoft(func, treeNode.lftChild);
        func.print((E) treeNode.data);
        if (!treeNode.deleted) {
            traverseSoft(func, treeNode.rtChild);
        }
    }



    // private helper methods ----------------------------------------
    protected LazySTNode<E> findMin( LazySTNode<E> root )
    {
        if (root == null)
            return null;
        if (root.lftChild == null)
            return root;
        return findMin(root.lftChild);
    }

    protected LazySTNode<E> findMax( LazySTNode<E> root )
    {
        if (root == null)
            return null;
        if (root.rtChild == null)
            return root;
        return findMax(root.rtChild);
    }

    protected LazySTNode<E> insert( LazySTNode<E> root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
        {
            mSizeHard++;
            mSize++;
            return new LazySTNode<E>(x, null, null, false);
        }

        compareResult = x.compareTo(root.data);
        if ( compareResult < 0 )
            root.lftChild = insert(root.lftChild, x);
        else if ( compareResult > 0 )
            root.rtChild = insert(root.rtChild, x);

        return root;
    }


    protected void remove(LazySTNode root, E x) {
        if (root == null)
            return;
        else if (find(root, x) != null) {
            find(root, x).deleted = true;
            mSize--;

        }
        return;
    }

    protected <F> void traverse(F func, LazySTNode<E> treeNode)
    {
        if (treeNode == null)
            return;

        traverse(func, treeNode.lftChild);
        traverse(func, treeNode.rtChild);
    }

    protected LazySTNode<E> find( LazySTNode<E> root, E x )
    {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
            return find(root.lftChild, x);
        if (compareResult > 0)
            return find(root.rtChild, x);
        return root;   // found
    }



    protected int findHeight( LazySTNode<E> treeNode, int height )
    {
        int leftHeight, rightHeight;
        if (treeNode == null)
            return height;
        height++;
        leftHeight = findHeight(treeNode.lftChild, height);
        rightHeight = findHeight(treeNode.rtChild, height);
        return (leftHeight > rightHeight)? leftHeight : rightHeight;
    }
    //inner class
    private class LazySTNode <E>{
        // use public access so the tree or other classes can access members
        protected LazySTNode<E> lftChild;
        protected LazySTNode<E> rtChild;
        protected E data;
        boolean deleted;

        protected LazySTNode(E d, LazySTNode<E> lft, LazySTNode<E> rt, boolean del )
        {

            this.lftChild = lft;
            this.rtChild = rt;
            deleted = del;
            data = d;
        }


    }

}



