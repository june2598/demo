package com.kh.demo.domain.product.dao;

import com.kh.demo.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor // final 필드를 매개값으로 갖는 생성자를 자동 생성해준다.
public class ProductDAOImpl implements ProductDAO {

  private final NamedParameterJdbcTemplate template;

//  public ProductDAOImpl(NamedParameterJdbcTemplate template){
//    this.template = template;
//  }

  @Override
  public Long save(Product product) {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("insert into product(product_id,pname,quantity,price) ");
    sql.append("values(product_product_id_seq.nextval, :pname, :quantity, :price) ");

    //sql수행
    //Map param = Map.of("pname",product.getPname(),"quantity",product.getQuantity(),"price",product.getPrice());
    // spring been객체(product)의 멤버필드와 sql의 매개변수(=파라미터) 이름을 매칭한 정보를 반환
    SqlParameterSource param = new BeanPropertySqlParameterSource(product);

    // template.update()가 수행된 레코드의 특정 컬럼값을 읽어오는 용도, id는 insert할때 생성되므로
    KeyHolder keyholder = new GeneratedKeyHolder();
    long rows = template.update(sql.toString(), param, keyholder, new String[]{"product_id"});
    log.info("rows={}", rows);
    //case1) 1개의 컬럼값을 읽어올때
//    long pid = keyholder.getKey().longValue();//상품아이디
    //case2) 2개 이상의 컬럼값을 읽어올때
    Number pidNumber = (Number) keyholder.getKeys().get("product_id");
    long pid = pidNumber.longValue();
    return pid;  //상품 아이디 반환
  }


//case1) 익숙한구조
//class MyRowMapper implements RowMapper{
//  @Override
//    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
//      //rs : select한 결과의 레코드 하나 , 수동매핑 (컬럼 라벨과 동일한 매칭이 안될때 직접 수동으로 세터를 통해 타게팅)
//      Product product = new Product();
//      product.setProductId(rs.getLong("product_id"));
//      product.setPrice(rs.getLong("price"));
//      product.setPname(rs.getString("pname"));
//      product.setQuantity(rs.getLong("quantity"));
//
//      return product;
//    }
//  };

  // MyRowMapper myrowMapper1 = new MyRowMapper();


  //case2) 익명클래스 - new RowMapper<Product>(){  <-- 중괄호 오는경우 : 인터페이스 혹은 추상메소드
//  RowMapper<Product> myRowMapper = new RowMapper<Product>(){
//    @Override
//    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
//      //rs : select한 결과의 레코드 하나 , 수동매핑 (컬럼 라벨과 동일한 매칭이 안될때 직접 수동으로 세터를 통해 타게팅)
//      Product product = new Product();
//      product.setProductId(rs.getLong("product_id"));
//      product.setPrice(rs.getLong("price"));
//      product.setPname(rs.getString("pname"));
//      product.setQuantity(rs.getLong("quantity"));
//
//      return product;
//    }
//  };

//  case3) 람다식 : RowMapper는 메소드가 하나밖에없는 인터페이스(기능이 하나인것) - 함수처럼 사용가능 (functional interface)
//  어차피 올게 하나밖에없기때문에, 당연한 부분을 생략한 형태
  RowMapper<Product> myRowMapper = (rs, rowNum) -> {
      Product product = new Product();
      product.setProductId(rs.getLong("product_id"));
      product.setPrice(rs.getLong("price"));
      product.setPname(rs.getString("pname"));
      product.setQuantity(rs.getLong("quantity"));
      return product;
  };

  //case4) 함수1
//
//  RowMapper<Product> productRowMapper() {
//    RowMapper<Product> myRowMapper = (rs, rowNum) ->{
//      Product product = new Product();
//      product.setProductId(rs.getLong("product_id"));
//      product.setPrice(rs.getLong("price"));
//      product.setPname(rs.getString("pname"));
//      product.setQuantity(rs.getLong("quantity"));
//      return product;
//    };
//    return myRowMapper;
//  }

  //case5)함수2

//  RowMapper<Product> productRowMapper() {
//    return (rs, rowNum) ->{
//      Product product = new Product();
//      product.setProductId(rs.getLong("product_id"));
//      product.setPrice(rs.getLong("price"));
//      product.setPname(rs.getString("pname"));
//      product.setQuantity(rs.getLong("quantity"));
//      return product;
//    };
//  }


  @Override
  public List<Product> findAll() {
    //sql
    StringBuffer sql = new StringBuffer();
    sql.append("select product_id,pname,quantity,price ");
    sql.append("  from product ");
    sql.append("order by product_id desc ");

    //db요청
    // BeanPropertyRowMapper : 자바 entity클래스와 db레코드를 자동(Bean~) 수동(myRow~) 매핑
    //동일한 타입의 entity클래스 (Product.class), 맴버필드랑 동일해야됨, 다를떈 append할떄 sql구문 (as 사용해서 필드 별칭을 고쳐서 이름을 같게)
    List<Product> list = template.query(sql.toString(), myRowMapper);

    return list;
  }

  @Override
  public List<Product> findAll(int reqPage, int reqRec) {
    StringBuffer sql = new StringBuffer();
    sql.append(" select product_id, pname, quantity, price ");
    sql.append(" from product ");
    sql.append(" offset  (:reqPage-1) * :reqRec rows fetch first :reqRec rows only ");

    Map<String, Integer> param = Map.of("reqPage", reqPage, "reqRec", reqRec);
    List<Product> list = template.query(sql.toString(), param, myRowMapper);

    return list;
  }

  @Override
  public Optional<Product> findById(Long productId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select product_id,pname,quantity,price ");
    sql.append("  from product ");
    sql.append(" where product_id = :productId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("productId", productId);

    Product product = null;

    try {
      product = template.queryForObject(
          sql.toString(),
          param,
          BeanPropertyRowMapper.newInstance(Product.class));
    } catch (EmptyResultDataAccessException e) { //조회 레코드가 없으면 예외 발생
      return Optional.empty();
    }

    return Optional.of(product);
  }

  @Override
  public int deleteById(Long productId) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from product ");
    sql.append("where product_id = :productId ");

    Map<String, Long> param = Map.of("productId", productId);
    int rows = template.update(sql.toString(), param);
    return rows;
  }

  @Override
  public int updateById(Long productId, Product product) {
    StringBuffer sql = new StringBuffer();
    sql.append("update product ");
    sql.append("set pname = :pname, quantity = :quantity, price = :price ");
    sql.append("where product_id = :productId ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("pname", product.getPname())
        .addValue("quantity", product.getQuantity())
        .addValue("price", product.getPrice())
        .addValue("productId", productId);

    int rows = template.update(sql.toString(), param);

    return rows;
  }

  @Override
  public int deleteByIds(List<Long> productIds) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from product ");
    sql.append(" where product_id in (:productIds) ");

    Map<String, List<Long>> param = Map.of("productIds", productIds);
    int rows = template.update(sql.toString(),param);

    return rows;
  }

  @Override
  public int totalRec() {
    String sql = "select count(*) from product ";
    return template.queryForObject(sql, Map.of(), Integer.class);

  }
}