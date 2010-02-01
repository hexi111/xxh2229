

k=1;
for j=1:n
    if(k==numofnode+1)
        k=1;
    end
    node_b(k,2)=node_b(k,2)+temp_b(j,3);
    node_b(k,3)=node_b(k,3)+temp_b(j,2);
    k=k+1;
end

t=mean(node_b(:,1));
for j=1:numofnode
    s(i,2)=s(i,2)+(node_b(j,1)-t)^2;
end

for j=1:numofnode
    if(node_b(j,2)>0)
        node_b(j,1)=node_b(j,1)+node_b(j,2)*interval/node_b(j,3);
        node_b(j,2)=node_b(j,2)-node_b(j,2)*interval/node_b(j,3);
        node_b(j,3)=node_b(j,3)-interval;
    end
end


