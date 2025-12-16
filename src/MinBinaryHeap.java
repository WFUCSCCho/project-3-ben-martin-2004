/**************************************************
 * @file: HorrorMovie.java
 * @description: Represents a horror movie record from the Kaggle dataset. Comparable based on rating (descending), then title.
 * @author: Ben Martin
 * @date: October 26, 2025
 **************************************************/
import java.util.ArrayList;
import java.util.Collection;

public class MinBinaryHeap<E extends Comparable<? super E>>
{
    private ArrayList<E> heap;

    public MinBinaryHeap()
    {
        heap = new ArrayList<>();
    }

    public MinBinaryHeap(Collection<? extends E> items)
    {
        heap = new ArrayList<>(items);
        heapify();
    }

    // ---------- basic methods ----------

    public int size()
    {
        return heap.size();
    }

    public boolean isEmpty()
    {
        return heap.isEmpty();
    }

    public boolean contains(E value)
    {
        return heap.contains(value);
    }

    public E peek()
    {
        if (heap.isEmpty())
        {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);
    }

    // ---------- modification methods ----------

    public void add(E value)
    {
        heap.add(value);
        percolateUp(heap.size() - 1);
    }

    public E poll()
    {
        if (heap.isEmpty())
        {
            throw new IllegalStateException("Heap is empty");
        }

        E root = heap.get(0);
        int lastIndex = heap.size() - 1;

        heap.set(0, heap.get(lastIndex));
        heap.remove(lastIndex);

        if (!heap.isEmpty())
        {
            percolateDown(0);
        }

        return root;
    }

    public boolean remove(E value)
    {
        int index = heap.indexOf(value);
        if (index == -1)
        {
            return false;
        }

        int lastIndex = heap.size() - 1;

        if (index == lastIndex)
        {
            heap.remove(lastIndex);
            return true;
        }

        heap.set(index, heap.get(lastIndex));
        heap.remove(lastIndex);

        // Restore heap property
        if (index > 0 &&
                heap.get(index).compareTo(heap.get(parent(index))) < 0)
        {
            percolateUp(index);
        }
        else
        {
            percolateDown(index);
        }

        return true;
    }

    // ---------- heapify ----------

    public void heapify()
    {
        for (int i = parent(heap.size() - 1); i >= 0; i--)
        {
            percolateDown(i);
        }
    }

    // ---------- percolation helpers ----------

    private void percolateUp(int index)
    {
        while (index > 0)
        {
            int parent = parent(index);
            if (heap.get(index).compareTo(heap.get(parent)) >= 0)
            {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    private void percolateDown(int index)
    {
        int size = heap.size();

        while (true)
        {
            int left = left(index);
            int right = right(index);
            int smallest = index;

            if (left < size &&
                    heap.get(left).compareTo(heap.get(smallest)) < 0)
            {
                smallest = left;
            }

            if (right < size &&
                    heap.get(right).compareTo(heap.get(smallest)) < 0)
            {
                smallest = right;
            }

            if (smallest == index)
            {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    // ---------- index helpers ----------

    private int parent(int index)
    {
        return (index - 1) / 2;
    }

    private int left(int index)
    {
        return 2 * index + 1;
    }

    private int right(int index)
    {
        return 2 * index + 2;
    }

    private void swap(int i, int j)
    {
        E temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
