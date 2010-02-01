

[x, y] = sort(temp_c(:,3));
temp_c = temp_c(y,:);

k=1;
for j=1:n
    if(k==numofnode+1)
        k=1;
    end
    node_c(k,2)=node_c(k,2)+temp_c(j,3);
    node_c(k,3)=node_c(k,3)+temp_c(j,2);
    k=k+1;
end

t=mean(node_c(:,1));
for j=1:numofnode
    s(i,3)=s(i,3)+(node_c(j,1)-t)^2;
end

for j=1:numofnode
    if(node_c(j,2)>0)
        node_c(j,1)=node_c(j,1)+node_c(j,2)*interval/node_c(j,3);
        node_c(j,2)=node_c(j,2)-node_c(j,2)*interval/node_c(j,3);
        node_c(j,3)=node_c(j,3)-interval;
    end
end

