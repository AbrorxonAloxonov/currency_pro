import React, { useEffect } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { saveAccountSettings, reset } from './settings.reducer';

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  const handleValidSubmit = values => {
    dispatch(
      saveAccountSettings({
        ...account,
        ...values,
      })
    );
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="settings-title">
            [<strong>{account.login}</strong>] sozlamalari
          </h2>
          <ValidatedForm id="settings-form" onSubmit={handleValidSubmit} defaultValues={account}>
            <ValidatedField
              name="firstName"
              label="Ism"
              id="firstName"
              placeholder="Ismingiz"
              validate={{
                required: { value: true, message: 'Ism kiriting.' },
                minLength: { value: 1, message: "Ism uzunligi kamida 1 harf bo'lsin" },
                maxLength: { value: 50, message: "Ism uzunligi ko'pida 50 harf bo'lsin" },
              }}
              data-cy="firstname"
            />
            <ValidatedField
              name="lastName"
              label="Familiya"
              id="lastName"
              placeholder="Familiyangiz"
              validate={{
                required: { value: true, message: 'Familiya kiriting.' },
                minLength: { value: 1, message: "Familiya uzunligi kamida 1 harf bo'lsin" },
                maxLength: { value: 50, message: "Familiya uzunligi ko'pida 50 harf bo'lsin" },
              }}
              data-cy="lastname"
            />
            <ValidatedField
              name="email"
              label="Email"
              placeholder="Sizning email"
              type="email"
              validate={{
                required: { value: true, message: 'Email kiritilishi zarur.' },
                minLength: { value: 5, message: 'Email uzunligi kamida 5 harfdan iborat bo`lsin' },
                maxLength: { value: 254, message: 'Email 50 harfdan uzun bo`lmasin' },
                validate: v => isEmail(v) || 'Iltimos, to`g`ri Email kiriting.',
              }}
              data-cy="email"
            />
            <Button color="primary" type="submit" data-cy="submit">
              Saqlash
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsPage;
