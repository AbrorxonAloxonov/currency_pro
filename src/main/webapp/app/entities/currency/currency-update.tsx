import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICurrency } from 'app/shared/model/currency.model';
import { getEntity, updateEntity, createEntity, reset } from './currency.reducer';

export const CurrencyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const currencyEntity = useAppSelector(state => state.currency.entity);
  const loading = useAppSelector(state => state.currency.loading);
  const updating = useAppSelector(state => state.currency.updating);
  const updateSuccess = useAppSelector(state => state.currency.updateSuccess);

  const handleClose = () => {
    navigate('/currency' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...currencyEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...currencyEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="currencyProApp.currency.home.createOrEditLabel" data-cy="CurrencyCreateUpdateHeading">
            Currencyni tahrirlash
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="currency-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Code" id="currency-code" name="code" data-cy="code" type="text" />
              <ValidatedField label="Ccy" id="currency-ccy" name="ccy" data-cy="ccy" type="text" />
              <ValidatedField label="Ccy Nm RU" id="currency-ccyNmRU" name="ccyNmRU" data-cy="ccyNmRU" type="text" />
              <ValidatedField label="Ccy Nm UZ" id="currency-ccyNmUZ" name="ccyNmUZ" data-cy="ccyNmUZ" type="text" />
              <ValidatedField label="Ccy Nm UZC" id="currency-ccyNmUZC" name="ccyNmUZC" data-cy="ccyNmUZC" type="text" />
              <ValidatedField label="Ccy Nm EN" id="currency-ccyNmEN" name="ccyNmEN" data-cy="ccyNmEN" type="text" />
              <ValidatedField label="Nominal" id="currency-nominal" name="nominal" data-cy="nominal" type="text" />
              <ValidatedField label="Rate" id="currency-rate" name="rate" data-cy="rate" type="text" />
              <ValidatedField label="Diff" id="currency-diff" name="diff" data-cy="diff" type="text" />
              <ValidatedField label="Date" id="currency-date" name="date" data-cy="date" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/currency" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Orqaga</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Saqla
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CurrencyUpdate;
