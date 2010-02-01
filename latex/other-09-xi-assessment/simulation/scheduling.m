clc;
clear;
type=[1,20,2.2;1,40,6.1;1,60,9;1,80,10.5;1,100,12.1;1,120,13.1;1,140,14.1;
    2,10,4;2,20,8;2,30,8;2,40,10;2,50,11;2,60,12;
    3,50,18;3,100,21;3,150,22;3,200,22;3,250,23;3,300,23;3,350,23;
    4,100,21;4,200,22;4,300,23;4,400,22;4,500,23;
    5,20,2;5,40,9;5,60,11;5,80,13;5,100,14;5,120,15;5,140,16;5,160,17;5,180,17;
    6,20,8;6,40,11.5;6,60,15;6,80,18;6,100,19;6,120,18.5;6,140,18;6,180,18];
 len=length(type);
 m=20;
 n=40;
 numofnode=10;
 interval=10;
 tasks=rand(m,n)*len;
 node=zeros(numofnode,3);
 s=zeros(m,5);
 node_b=node;
 node_a=node;
 node_c=node;
 node_d=node;
 node_e=node;
 
 max=30000;
 history=zeros(numofnode,max);
 for i=2:max
     for j=1:numofnode
     history(j,i)=-1;
     end
 end
 totaltime=1;
 
 for i=1 : m
     temp=zeros(n,3);
     for j=1 :n
     temp(j,:)=type(ceil(tasks(i,j)),:);
     end
     temp_b=temp;
     temp_a=temp;
     temp_c=temp;
     temp_d=temp;
     temp_e=temp;
     algorithmA;
     algorithmB;
     algorithmC;
     algorithmD;
     algorithmE;
 end
 sum(s(:,1))
 sum(s(:,2))
 %sum(s(:,3))
 %sum(s(:,4))
 %sum(s(:,5))