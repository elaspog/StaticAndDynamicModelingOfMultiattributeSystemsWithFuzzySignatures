
var width = 960;
var height = 500;
var radius = 10;
var linkDistance = 100;
var isCurved = false;
var nodes = [];
var links = [];


d3.select("#zoom-out").on("click", function(){
	if (linkDistance >= 75) {
			linkDistance = linkDistance - 25;
		}
		visualize();
	})

d3.select("#zoom-in").on("click", function(){
	linkDistance = linkDistance + 25;
	visualize();
});

d3.select("#set-curve").on("click", function(){
	if (isCurved == true)
		isCurved = false;
	else
		isCurved = true;
	visualize();
});

d3.select("#reset").on("click", function(){
	linkDistance = 100;
	visualize();
});


function initialize(statesStr, transitionsStr, w, h) {

	states = JSON.parse(statesStr);
	transitions = JSON.parse(transitionsStr);

	width = w;
	height = h;

	nodes = [];
	links = [];

	states.forEach(function(state) {
		nodes.push( state );
	});

	transitions.forEach(function(transition) {
		sources = nodes.filter( function(x) {return x.name === transition.from;} );
		targets = nodes.filter( function(x) {return x.name === transition.to;} );
		links.push({"source":sources[0], "target":targets[0], type:"directed"});
	});
}


function visualize() {

	d3.selectAll("svg").remove();

	force = d3.layout.force()
		.size([width, height])
		.charge(-400)
		.linkDistance(linkDistance)
		.on("tick", tick);

	drag = force.drag()
		.on("dragstart", dragstart);

	svg = d3.select("body").append("svg")
		.attr("width", width)
		.attr("height", height);

	link = svg.selectAll(".link");
	node = svg.selectAll(".node");

	force
		.nodes(nodes)
		.links(links)
		.start();

	// Per-type markers, as they don't inherit styles.
	svg.append("defs").selectAll("marker")
			.data(["directed", "directed_dotted", "directed_green"])
		.enter().append("marker")
			.attr("id", function(d) { return d; })
			.attr("viewBox", "0 -5 10 10")
			.attr("refX", 15)
			.attr("refY", -1.5)
			.attr("markerWidth", radius)
			.attr("markerHeight", radius)
			.attr("orient", "auto")
		.append("path")
			.attr("d", "M0,-3L10,0L0,3");

	link = svg.append("g").selectAll(".link")
			.data(force.links())
		.enter().append("path")
			.attr("class", function(d) { return "link " + d.type; })
			.attr("marker-end", function(d) { return "url(#" + d.type + ")"; });

	node = svg.append("g").selectAll(".node")
			.data(force.nodes())
		.enter().append("circle")
			.attr("r", radius)
			.attr("class", function(d) { return d.type; })
			.on("dblclick", dblclick)
			.call(force.drag);

	text = svg.append("g").selectAll("text")
			.data(force.nodes())
		.enter().append("text")
			.attr("x", 8)
			.attr("y", ".31em")
			.text(function(d) { return d.name; });

}

// Use elliptical arc path segments to doubly-encode directionality.
function tick() {
	link.attr("d", linkArc);
	node.attr("transform", transform);
	text.attr("transform", transform);
}

function linkArc(d) {

	if (isCurved == true) {
		var dx = d.target.x - d.source.x,
		dy = d.target.y - d.source.y,
		dr = Math.sqrt(dx * dx + dy * dy);
		return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
	} else {
		return "M" + d.source.x + "," + d.source.y + "L" + d.target.x + "," + d.target.y;
	}
}

function transform(d) {
	return "translate(" + d.x + "," + d.y + ")";
}

function dblclick(d) {
	d3.select(this).classed("fixed", d.fixed = false);
}

function dragstart(d) {
	d3.select(this).classed("fixed", d.fixed = true);
}

