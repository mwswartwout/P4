package p4;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class puzzle 
{
    private node[][] puzzle = new node[3][3];
    private node[] pieces = new node[9];
    private puzzle parent;
    
    public puzzle()
    {
        int count = 0;
        
        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                pieces[count] = new node(count, r, c);
                count++;
            }
        }
        
        updatePuzzle();
    }
    
    public puzzle(node[] array)
    {
        System.arraycopy(array, 0, pieces, 0, 9);
        
        updatePuzzle();
    }
    
    public puzzle(node[] array, puzzle p)
    {
        System.arraycopy(array, 0, pieces, 0, 9);
        parent = p;
        updatePuzzle();
    }
    
    private void updatePuzzle()
    {
        for (int i = 0; i < 9; i++)
        {
            puzzle[pieces[i].getRow()][pieces[i].getCol()] = pieces[i];
        }
    }
    
    public void print()
    {
        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 3; c++)
            {
                System.out.print(puzzle[r][c].getVal() +" ");
            }
            
            System.out.println();
        }
        
        System.out.println();
    }
    
    public puzzle getParent()
    {
        return parent;
    }
    
    public void setParent(puzzle p)
    {
        parent = p;
    }
    
    public void setPieces(node[] array)
    {
        pieces = array;
        updatePuzzle();
    }
    
    public node[] getPieces()
    {
        return pieces;
    }
    
    public node[][] getPuzzle()
    {
        return puzzle;
    }
    public puzzle upChild(puzzle puz)
    {
        puzzle upChild = new puzzle();
        upChild.setParent(puz);
        
        if (findHole().getRow() == 0 || puz.getPieces() == null)
        {
            return null;
        }
        
        else
        {   
            findHole().setRow(findHole().getRow() - 1);
            
            for (int i = 0; i < 9; i++)
            {
                if (pieces[i].getRow() == findHole().getRow() && pieces[i].getCol() == findHole().getCol() && pieces[i] != findHole())
                {
                    pieces[i].setRow(pieces[i].getRow() + 1);
                }
            }
            
            upChild.setPieces(pieces);
            
            return upChild;   
        }
    }
    
    public puzzle downChild(puzzle puz)
    {
        puzzle downChild = new puzzle();
        downChild.setParent(puz);
        
        if (findHole().getRow() == 2)
        {
            return null;
        }
        
        else
        {
            findHole().setRow(findHole().getRow() + 1);
            
            for (int i = 0; i < 9; i++)
            {
                if (pieces[i].getRow() == findHole().getRow() && pieces[i].getCol() == findHole().getCol() && pieces[i] != findHole())
                {
                    pieces[i].setRow(pieces[i].getRow() - 1);
                }
            }
            
            downChild.setPieces(pieces);
            
            return downChild;
        }
    }
    
    public puzzle rightChild(puzzle puz)
    {
        puzzle rightChild = new puzzle(pieces);
        rightChild.setParent(puz);
        
        if (findHole().getCol() == 2)
        {
            return null;
        }
        
        else
        {
            findHole().setCol(findHole().getCol() + 1);
            
            for (int i = 0; i < 9; i++)
            {
                if (pieces[i].getRow() == findHole().getRow() && pieces[i].getCol() == findHole().getCol() && pieces[i] != findHole())
                {
                    pieces[i].setCol(pieces[i].getCol() - 1);
                }
            }
            
            rightChild.setPieces(pieces);
            
            return rightChild;
        }
    }
    
    public puzzle leftChild(puzzle puz)
    {
        puzzle leftChild = new puzzle(pieces);
        leftChild.setParent(puz);
        
        if (findHole().getCol() == 0)
        {
            return null;
        }
        
        else
        {
            findHole().setCol(findHole().getCol() - 1);
            
            for (int i = 0; i < 9; i++)
            {
                if (pieces[i].getRow() == findHole().getRow() && pieces[i].getCol() == findHole().getCol() && pieces[i] != findHole())
                {
                    pieces[i].setCol(pieces[i].getCol() + 1);
                }
            }
            
            leftChild.setPieces(pieces);
            
            return leftChild;
        }
    }
    
    public puzzle randomize(puzzle puz)
    {
        
        boolean moved;
        int move;
        Random generator = new Random();

        puzzle oldPuzzle = puz;
        
        for (int i = 0; i < 31; i++)
        {
            moved = false;
            
            puzzle newPuzzle = new puzzle();
            
            while ( !moved )
            {
                move = generator.nextInt(4);
                
                if (move == 0 && upChild(oldPuzzle) != null)
                {
                    newPuzzle = upChild(oldPuzzle);
                    moved = true;
                }
                
                if (move == 1 && downChild(oldPuzzle) != null)
                {
                    newPuzzle = downChild(oldPuzzle);
                    moved = true;
                }
                
                if (move == 2 && rightChild(oldPuzzle) != null)
                {
                    newPuzzle = rightChild(oldPuzzle);
                    moved = true;
                }
                
                if (move == 3 && leftChild(oldPuzzle) != null)
                {
                    newPuzzle = leftChild(oldPuzzle);
                    moved = true;
                }
            }
            
            oldPuzzle = new puzzle(newPuzzle.getPieces(), newPuzzle);
            
        }
       
        
        return oldPuzzle;
    }
    
    public void breadthFirstSearch(puzzle puz)
    {
        boolean solved = false;
        
        puzzle solution = new puzzle();
        puzzle initial = new puzzle(puz.getPieces());
        
        puzzle currentPuz = new puzzle();
        Queue<puzzle> queue = new LinkedList<>();
        queue.add(puz);

        int count = 0;
        
        while (count < 31 && !solved)
        {
            System.out.println("while loop iterated, count = " +count);
            
            if (queue.peek() == null)
            {
                System.out.println("going to break");
                break;
            }
            
            currentPuz = queue.poll();
            System.out.println("queue polled");
            
            if (upChild(currentPuz).getPuzzle() != null)
            {
                System.out.println("up child not null");
                
                if (Arrays.deepEquals(upChild(currentPuz).getPuzzle(), solution.getPuzzle()))
                {
                    System.out.println("solved");
                    solved = true;
                }
                
                
                else if (!Arrays.deepEquals(upChild(currentPuz).getPuzzle(), initial.getPuzzle()))
                {
                    System.out.println("added up child to queue");
                    queue.add(upChild(currentPuz));
                }
            }
            
            System.out.println("up child check complete");
            
            if (downChild(currentPuz).getPuzzle() != null)
            {
                if (Arrays.deepEquals(downChild(currentPuz).getPuzzle(), solution.getPuzzle()))
                {
                    solved = true;
                }
            
                else if (!Arrays.deepEquals(downChild(currentPuz).getPuzzle(), initial.getPuzzle()))
                {
                    queue.add(downChild(currentPuz));
                }
            }
            
            if (leftChild(currentPuz) != null)
            {
                if (Arrays.deepEquals(leftChild(currentPuz).getPuzzle(), solution.getPuzzle()))
                {
                    solved = true;
                }
            
                else if (!Arrays.deepEquals(leftChild(currentPuz).getPuzzle(), initial.getPuzzle()))
                {
                    queue.add(leftChild(currentPuz));
                }
            }
            
            if (rightChild(currentPuz) != null)
            {
                if (Arrays.deepEquals(rightChild(currentPuz).getPuzzle(), solution.getPuzzle()))
                {
                    solved = true;
                }
            
                else if (!Arrays.deepEquals(rightChild(currentPuz).getPuzzle(), initial.getPuzzle()))
                {
                    queue.add(rightChild(currentPuz));
                }
            }
            count++;
        }
        
        if (solved == true)
        {
            currentPuz.print();
            
            while( currentPuz.getParent() != null)
            {
                currentPuz = currentPuz.getParent();
                currentPuz.print();
            }
        }
    }
    
    public void depthFirstSearch(puzzle puz, puzzle initial, int n)
    {
        int count = n;
        
        puzzle solution = new puzzle();
        
        if (Arrays.deepEquals(puz.getPuzzle(), solution.getPuzzle()))
        {
            System.out.println("Solution found");
            puz.print();
        
            while( puz.getParent() != null)
            {
                puz = puz.getParent();
                puz.print();
            }
        }
        
        else if (n <= 5 && (!Arrays.deepEquals(puz.getPuzzle(), initial.getPuzzle()) || n == 0))
        {
            if (upChild(puz) != null)
            {
                depthFirstSearch(upChild(puz), initial, count++);
            }
            
            if (downChild(puz) != null)
            {
                //System.out.println
                depthFirstSearch(downChild(puz), initial, count++);
            }
            
            if (leftChild(puz) != null)
            {
                depthFirstSearch(leftChild(puz), initial, count++);
            }
            
            if (rightChild(puz) != null)
            {
                  depthFirstSearch(rightChild(puz), initial, count++);      
            }
        }
    }
    
    public node findHole()
    {
        for (int i = 0; i < 9; i++)
        {
            if (pieces[i].getVal() == 0)
            {
                return pieces[i];
            }
        }
        
        return null;
    }
    
    public void printPieces()
    {
        for (int i = 0; i < 9; i++)
        {
            System.out.println(pieces[i].getVal() +" " +pieces[i].getRow() +" " +pieces[i].getCol());
        }
        
        System.out.println();
    }
}
