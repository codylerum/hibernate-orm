/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.graph.spi;

import org.hibernate.graph.RootGraph;
import org.hibernate.metamodel.model.domain.EntityDomainType;
import org.hibernate.metamodel.model.domain.JpaMetamodel;
import org.hibernate.metamodel.model.domain.ManagedDomainType;

/**
 * Integration version of the {@link RootGraph} contract
 *
 * @author Steve Ebersole
 */
public interface RootGraphImplementor<J> extends RootGraph<J>, GraphImplementor<J> {

	boolean appliesTo(String entityName, JpaMetamodel metamodel);

	boolean appliesTo(EntityDomainType<?> entityType, JpaMetamodel metamodel);

	@Override
	default boolean appliesTo(ManagedDomainType<?> managedType, JpaMetamodel metamodel) {
		assert managedType instanceof EntityDomainType;
		return appliesTo( (EntityDomainType<?>) managedType, metamodel );
	}

	@Override
	RootGraphImplementor<J> makeRootGraph(String name, boolean mutable);

	@Override
	SubGraphImplementor<J> makeSubGraph(boolean mutable);

	/**
	 * Make an immutable copy of this entity graph, using the given name.
	 *
	 * @param name The name to apply to the immutable copy
	 *
	 * @return The immutable copy
	 */
	default RootGraphImplementor<J> makeImmutableCopy(String name) {
		return makeRootGraph( name, false );
	}
}
