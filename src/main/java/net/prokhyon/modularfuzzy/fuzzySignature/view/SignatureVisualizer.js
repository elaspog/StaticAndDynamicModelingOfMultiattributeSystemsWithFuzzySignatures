
var width, height, padding;
var nodes = [];
var links = [];

function initialize(dataStr, w, h, p){

	width = w;
	height = h;
	padding = p;

	data = JSON.parse(dataStr);

	var tree = d3.layout.tree()
		.size([width - padding, height - padding]);

	nodes = tree.nodes(data);
	links = tree.links(nodes);
}

function visualize(){

	d3.selectAll("svg").remove();

	var canvas = d3.select("body").append("svg")
		.attr("width", width)
		.attr("height", height)
		.append("g")
			.attr("transform", "translate(" + padding/2 + "," + padding/2 + ")");

	var node = canvas.selectAll(".node")
		.data(nodes)
		.enter()
		.append("g")
			.attr("class", "node")
			.attr("transform", function(d){ return "translate(" + d.x + ","+ d.y + ")"; });

	node.append("circle")
		.attr("r", 6)
		.attr("fill", "steelblue");

	node.append("text")
		.text(function(d) { return d.name; })

	var diagonal = d3.svg.diagonal();

	canvas.selectAll(".link")
		.data(links)
		.enter()
		.append("path")
		.attr("class","link")
		.attr("fill","none")
		.attr("stroke","#ADADAD")
		.attr("d", diagonal);

}
