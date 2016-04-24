package Database;

import Database.SnpRatio;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-03-01T15:49:26")
@StaticMetamodel(SnpRatio.class)
public class SnpRatio_ { 

    public static volatile SingularAttribute<SnpRatio, SnpRatio> allPermutation;
    public static volatile SingularAttribute<SnpRatio, String> snipId;
    public static volatile CollectionAttribute<SnpRatio, SnpRatio> snpRatioCollection;
    public static volatile CollectionAttribute<SnpRatio, SnpRatio> snpRatioCollection1;
    public static volatile SingularAttribute<SnpRatio, Integer> id;
    public static volatile SingularAttribute<SnpRatio, String> type;
    public static volatile SingularAttribute<SnpRatio, SnpRatio> validPermutation;
    public static volatile SingularAttribute<SnpRatio, Date> updated;
    public static volatile SingularAttribute<SnpRatio, Double> ratio;

}