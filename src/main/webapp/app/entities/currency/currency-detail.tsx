import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './currency.reducer';

export const CurrencyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const currencyEntity = useAppSelector(state => state.currency.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="currencyDetailsHeading">Currency</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{currencyEntity.id}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{currencyEntity.code}</dd>
          <dt>
            <span id="ccy">Ccy</span>
          </dt>
          <dd>{currencyEntity.ccy}</dd>
          <dt>
            <span id="ccyNmRU">Ccy Nm RU</span>
          </dt>
          <dd>{currencyEntity.ccyNmRU}</dd>
          <dt>
            <span id="ccyNmUZ">Ccy Nm UZ</span>
          </dt>
          <dd>{currencyEntity.ccyNmUZ}</dd>
          <dt>
            <span id="ccyNmUZC">Ccy Nm UZC</span>
          </dt>
          <dd>{currencyEntity.ccyNmUZC}</dd>
          <dt>
            <span id="ccyNmEN">Ccy Nm EN</span>
          </dt>
          <dd>{currencyEntity.ccyNmEN}</dd>
          <dt>
            <span id="nominal">Nominal</span>
          </dt>
          <dd>{currencyEntity.nominal}</dd>
          <dt>
            <span id="rate">Rate</span>
          </dt>
          <dd>{currencyEntity.rate}</dd>
          <dt>
            <span id="diff">Diff</span>
          </dt>
          <dd>{currencyEntity.diff}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{currencyEntity.date}</dd>
        </dl>
        <Button tag={Link} to="/currency" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Orqaga</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/currency/${currencyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">O`zgartir</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CurrencyDetail;
