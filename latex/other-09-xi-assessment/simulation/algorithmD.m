
[x, y] = sort(temp_d(:,3));
temp_d = temp_d(y,:);
[w, z] = sort(node_d(:,1),'descend');
node_d = node_d(z,:);

k=1;
for j=1:n
    if(k==numofnode+1)
        k=1;
    end
    node_d(k,2)=node_d(k,2)+temp_d(j,3);
    node_d(k,3)=node_d(k,3)+temp_d(j,2);
    k=k+1;
end

t=mean(node_d(:,1));
for j=1:numofnode
    s(i,4)=s(i,4)+(node_d(j,1)-t)^2;
end

for j=1:numofnode
    if(node_d(j,2)>0)
        node_d(j,1)=node_d(j,1)+node_d(j,2)*interval/node_d(j,3);
        node_d(j,2)=node_d(j,2)-node_d(j,2)*interval/node_d(j,3);
        node_d(j,3)=node_d(j,3)-interval;
    end
end

