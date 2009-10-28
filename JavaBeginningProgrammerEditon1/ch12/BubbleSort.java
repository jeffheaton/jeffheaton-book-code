/**
 * BubbleSort
 * Copyright 2006 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * Example program from Chapter 12
 * Java for the Beginning Programmer
 * http://www.heatonresearch.com/articles/series/15/
 *
 * This class shows how to implement a bubble sort.
 *
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not
 * publish the source code without the author's permission.
 * For more information on distributing this code, please
 * visit:
 *    http://www.heatonresearch.com/hr_legal.php
 *
 * @author Jeff Heaton
 * @version 1.1
 */
 
class BubbleSort 
{ 
  public static void bubbleSort(String data[],int 
size) 
  { 
    boolean done = false; 
    int index;

    while (done == false) 
    { 
      done = true; 
      for (index=1 ; index < size;index++) 
      { 
        if (data[index].compareTo(data[index-1])<0) 
        { 
          // swap data[index] and data[index-1] 
          String temp          = data[index]; 
          data[index]   = data[index-1]; 
          data[index-1] = temp; 
          done = false; 
        } 
      } 
    } 
  } 
  public static void main(String args[]) 
  { 
    String str[] = new String[5]; 
    // unsorted array 
    str[0]="John"; 
    str[1]="George"; 
    str[2]="Simon"; 
    str[3]="Alice"; 
    str[4]="Betty"; 
    // now bubble sort 
    bubbleSort(str,5); 
    // display the results 
    for(int i=0;i<5;i++) 
      System.out.println(str[i]); 
  } 
} 
