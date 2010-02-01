
[x, y] = sort(temp_a(:,3));
temp_a = temp_a(y,:);
[w, z] = sort(node_a(:,1));
node_a = node_a(z,:);

k=1;
for j=1:n
    if(k==numofnode+1)
        k=1;
    end
    node_a(k,2)=node_a(k,2)+temp_a(j,3);
    node_a(k,3)=node_a(k,3)+temp_a(j,2);
    k=k+1;
end

t=mean(node_a(:,1));
for j=1:numofnode
    s(i,1)=s(i,1)+(node_a(j,1)-t)^2;
end

for j=1:numofnode
    if(node_a(j,2)>0)
        node_a(j,1)=node_a(j,1)+node_a(j,2)*interval/node_a(j,3);
        node_a(j,2)=node_a(j,2)-node_a(j,2)*interval/node_a(j,3);
        node_a(j,3)=node_a(j,3)-interval;
    end
end

