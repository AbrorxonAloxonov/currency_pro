import React, { useEffect } from 'react';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Button, Alert, Col, Row } from 'reactstrap';
import { toast } from 'react-toastify';

import { handlePasswordResetInit, reset } from '../password-reset.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PasswordResetInit = () => {
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );

  const handleValidSubmit = ({ email }) => {
    dispatch(handlePasswordResetInit(email));
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>Parolni tiklash</h1>
          <Alert color="warning">
            <p>Ro&apos;yhatdan o&apos;tgan email kirting</p>
          </Alert>
          <ValidatedForm onSubmit={handleValidSubmit}>
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
              data-cy="emailResetPassword"
            />
            <Button color="primary" type="submit" data-cy="submit">
              Parolni tiklash
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetInit;
