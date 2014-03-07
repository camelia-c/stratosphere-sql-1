package eu.stratosphere.sql.rules;

import org.eigenbase.rel.AggregateRel;
import org.eigenbase.rel.ProjectRel;
import org.eigenbase.rel.RelNode;
import org.eigenbase.rel.convert.ConverterRule;
import org.eigenbase.relopt.Convention;
import org.eigenbase.relopt.RelTraitSet;

import eu.stratosphere.sql.relOpt.StratosphereRel;
import eu.stratosphere.sql.relOpt.StratosphereSqlAggregation;
import eu.stratosphere.sql.relOpt.StratosphereSqlProjection;


public class StratosphereAggregateRule extends ConverterRule {

	public static StratosphereAggregateRule INSTANCE = new StratosphereAggregateRule();
	
	/**
	 *  public ConverterRule(
      Class<? extends RelNode> clazz,
      RelTrait in,
      RelTrait out,
      String description) {
	 */
	public StratosphereAggregateRule() {
		 super(AggregateRel.class,
		          Convention.NONE,
		          StratosphereRel.CONVENTION,
		          "StratosphereAggregateRule");
	}

	@Override
	public RelNode convert(RelNode rel) {
		System.err.println("Converting aggregate");
		final AggregateRel aggregate = (AggregateRel) rel;
		final RelTraitSet traits = aggregate.getTraitSet().plus(StratosphereRel.CONVENTION);
		return new StratosphereSqlAggregation(aggregate.getCluster(), traits, aggregate.getChild(), aggregate.getGroupSet(), aggregate.getAggCallList());
	}

}
